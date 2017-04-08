package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.client.nio.connection.NioConnector;
import org.qfox.jestful.client.nio.scheduler.NioScheduler;
import org.qfox.jestful.client.nio.timeout.TimeoutManager;
import org.qfox.jestful.client.nio.timeout.TreeSetTimeoutManager;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class NioClient extends Client implements Runnable, NioCalls.NioConsumer, NioConnector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Object startupLock = new Object();
    private Exception startupException;
    private static NioClient defaultClient;
    private Selector selector;
    private NioCalls calls;
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);
    private final long selectTimeout;
    private final TimeoutManager timeoutManager;
    private final SSLContext sslContext;

    private NioClient(Builder<?> builder) {
        super(builder);
        this.selectTimeout = builder.selectTimeout;
        this.timeoutManager = builder.timeoutManager;
        this.sslContext = builder.sslContext;
        synchronized (startupLock) {
            try {
                new Thread(this).start();
                startupLock.wait();
                if (startupException != null) throw startupException;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void consume(SocketAddress address, Action action) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(address);
            SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT, action);
            NioRequest request = (NioRequest) action.getExtra().get(NioRequest.class);
            timeoutManager.addConnTimeoutHandler(key, request.getConnTimeout());
        } catch (Exception e) {
            NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
            listener.onException(action, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (selector != null) selector.wakeup();
    }

    @Override
    public void run() {
        // 启动
        synchronized (startupLock) {
            try {
                selector = Selector.open();
                calls = new NioCalls(selector);
            } catch (IOException e) {
                startupException = e;
                return;
            } finally {
                startupLock.notify();
            }
        }
        // 运行
        while (!isDestroyed()) {
            SelectionKey key = null;
            try {
                // 处理超时
                timeoutManager.fire();
                // 处理注册
                calls.foreach(this);
                // 最多等待 selected timeout 时间进行一次超时检查
                if (selector.select(selectTimeout) == 0) {
                    continue;
                }

                if (isDestroyed()) {
                    break;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        key.cancel();
                        continue;
                    }
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Action action = (Action) key.attachment();
                        NioRequest request = (NioRequest) action.getExtra().get(NioRequest.class);
                        if (channel.isConnectionPending() && channel.finishConnect()) {
                            // 本来HTTP 模式情况下这里只需要注册WRITE即可 但是为了适配SSL模式的握手过程的握手数据读写 这里必须注册成WRITE | READ 但是只计算Write Timeout
                            channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, action);
                            timeoutManager.addWriteTimeoutHandler(key, request.getWriteTimeout());

                            NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                            listener.onConnected(action);
                        }
                    }
                    if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Action action = (Action) key.attachment();
                        NioRequest request = (NioRequest) action.getExtra().get(NioRequest.class);
                        buffer.clear();
                        request.copy(buffer);
                        buffer.flip();
                        int n = channel.write(buffer);
                        if (request.move(n)) {
                            // 请求发送成功后只注册READ把之前的 WRITE | READ 注销掉 开始计算Read Timeout
                            channel.register(selector, SelectionKey.OP_READ, action);
                            timeoutManager.addReadTimeoutHandler(key, request.getReadTimeout());

                            NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                            listener.onRequested(action);
                        }
                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Action action = (Action) key.attachment();
                        NioResponse response = (NioResponse) action.getExtra().get(NioResponse.class);
                        buffer.clear();
                        channel.read(buffer);
                        buffer.flip();
                        boolean finished = response.load(buffer);
                        if (finished) {
                            key.cancel();
                            IOKit.close(key.channel());

                            NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                            listener.onCompleted(action);
                        }
                    }
                }
            } catch (Exception e) {
                if (key != null) {
                    key.cancel();
                    IOKit.close(key.channel());
                    try {
                        Action action = (Action) key.attachment();
                        NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                        listener.onException(action, e);
                    } catch (RuntimeException re) {
                        logger.warn("", re);
                    }
                }
                logger.warn("", e);
            } catch (Error e) {
                if (key != null) key.cancel();
                logger.error("", e);
            } catch (Throwable t) {
                if (key != null) key.cancel();
                logger.error("", t);
            }
        }
        try {
            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object react(Action action) throws Exception {
        String protocol = action.getProtocol();
        String host = action.getHost();
        Integer port = action.getPort();
        port = port != null && port >= 0 ? port : "https".equalsIgnoreCase(protocol) ? 443 : "http".equalsIgnoreCase(protocol) ? 80 : 0;
        Gateway gateway = this.getGateway();
        SocketAddress address = gateway != null && gateway.isProxy() ? gateway.toSocketAddress() : new InetSocketAddress(host, port);
        (gateway != null ? gateway : Gateway.NULL).onConnected(action);
        NioEventListener listener = new JestfulNioEventListener();
        action.getExtra().put(NioEventListener.class, listener);
        calls.offer(address, action);
        return null;
    }

    public Creator<?> creator() {
        return new Creator();
    }

    public class Creator<C extends Creator<C>> extends Client.Creator<C> {
        @Override
        public <T> T create(Class<T> interfase, URL endpoint) {
            String protocol = endpoint.getProtocol();
            String host = endpoint.getHost();
            Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
            String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
            return new JestfulNioInvocationHandler<T>(interfase, protocol, host, port, route, NioClient.this).getProxy();
        }
    }

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        NioConnection connection = (NioConnection) action.getExtra().get(NioConnection.class);
        if (connection != null) {
            return connection;
        }
        for (Connector connector : connectors.values()) {
            if (connector instanceof NioConnector && connector.supports(action)) {
                NioConnector nioConnector = (NioConnector) connector;
                connection = nioConnector.nioConnect(action, gateway, this);
                action.getExtra().put(NioConnection.class, connection);
                return connection;
            }
        }
        throw new IOException("unsupported protocol " + action.getProtocol());
    }

    public static NioClient getDefaultClient() {
        if (defaultClient != null) {
            return defaultClient;
        }
        synchronized (Client.class) {
            if (defaultClient != null) {
                return defaultClient;
            }
            return defaultClient = builder().build();
        }
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> extends Client.Builder<T> {
        private long selectTimeout = 1000L;
        private TimeoutManager timeoutManager = new TreeSetTimeoutManager();
        private SSLContext sslContext;

        public Builder() {
            this.setConnTimeout(20 * 1000);
            this.setReadTimeout(Integer.MAX_VALUE);
            this.setWriteTimeout(Integer.MAX_VALUE);
            String userAgent = "Mozilla/5.0"
                    + " "
                    + "("
                    + System.getProperty("os.name")
                    + " "
                    + System.getProperty("os.version")
                    + "; "
                    + System.getProperty("os.arch")
                    + "; "
                    + System.getProperty("user.language")
                    + ")"
                    + " "
                    + Module.getInstance().getParentName()
                    + "/"
                    + Module.getInstance().getParentVersion()
                    + " "
                    + Module.getInstance().getName()
                    + "/"
                    + Module.getInstance().getVersion();
            this.setUserAgent(userAgent);
        }

        @Override
        public NioClient build() {
            return new NioClient(this);
        }

        public T setSelectTimeout(long selectTimeout) {
            if (selectTimeout < 0) {
                throw new IllegalArgumentException("selected timeout is negative");
            }
            this.selectTimeout = selectTimeout;
            return (T) this;
        }

        public T setTimeoutManager(TimeoutManager timeoutManager) {
            if (timeoutManager == null) {
                throw new IllegalArgumentException("timeout manager can not be null");
            }
            this.timeoutManager = timeoutManager;
            return (T) this;
        }

        public T setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            if (sslContext == null) {
                throw new IllegalArgumentException("SSLContext can not be null");
            }
            return (T) this;
        }
    }

    private class JestfulNioEventListener extends NioEventAdapter {

        @Override
        public void onConnected(Action action) throws Exception {
            Request request = action.getRequest();
            Restful restful = action.getRestful();

            if (restful.isAcceptBody()) {
                serialize(action);
            } else {
                request.connect();
            }
        }

        @Override
        public void onRequested(Action action) throws Exception {
            Response response = action.getResponse();
            if (!response.isResponseSuccess()) {
                String contentType = response.getContentType();
                MediaType mediaType = MediaType.valueOf(contentType);
                String charset = mediaType.getCharset();
                if (charset == null || charset.length() == 0) {
                    charset = response.getResponseHeader("Content-Charset");
                }
                if (charset == null || charset.length() == 0) {
                    charset = response.getCharacterEncoding();
                }
                if (charset == null || charset.length() == 0) {
                    charset = java.nio.charset.Charset.defaultCharset().name();
                }
                Status status = response.getResponseStatus();
                InputStream in = response.getResponseInputStream();
                InputStreamReader reader = in == null ? null : new InputStreamReader(in, charset);
                String body = reader != null ? IOKit.toString(reader) : "";
                throw new UnexpectedStatusException(status, body);
            }
        }

        @Override
        public void onCompleted(Action action) throws Exception {
            Request request = action.getRequest();
            Response response = action.getResponse();
            IOKit.close(request);
            IOKit.close(response);
            onRequested(action);

            // 回应
            Restful restful = action.getRestful();
            if (restful.isReturnBody()) {
                deserialize(action);
            } else {
                Map<String, String> header = new CaseInsensitiveMap<String, String>();
                for (String key : response.getHeaderKeys()) {
                    String name = key != null ? key : "";
                    String value = response.getResponseHeader(key);
                    header.put(name, value);
                }
                action.getResult().getBody().setValue(header);
            }

            NioScheduler scheduler = (NioScheduler) action.getExtra().get(Scheduler.class);
            scheduler.doCallbackSchedule(NioClient.this, action);
        }

        @Override
        public void onException(Action action, Exception exception) {
            Request request = action.getRequest();
            Response response = action.getResponse();
            IOKit.close(request);
            IOKit.close(response);

            try {
                action.getResult().setException(exception);

                NioScheduler scheduler = (NioScheduler) action.getExtra().get(Scheduler.class);
                scheduler.doCallbackSchedule(NioClient.this, action);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getSelectTimeout() {
        return selectTimeout;
    }

    public TimeoutManager getTimeoutManager() {
        return timeoutManager;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
