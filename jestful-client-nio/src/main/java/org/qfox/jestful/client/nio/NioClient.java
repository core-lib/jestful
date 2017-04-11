package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.balancer.NioBalancer;
import org.qfox.jestful.client.nio.balancer.RandomNioBalancer;
import org.qfox.jestful.client.nio.catcher.NioCatcher;
import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.client.nio.connection.NioConnector;
import org.qfox.jestful.client.nio.scheduler.NioScheduler;
import org.qfox.jestful.client.nio.timeout.SortedTimeoutManager;
import org.qfox.jestful.client.nio.timeout.TimeoutManager;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class NioClient extends Client implements NioConnector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static NioClient defaultClient;
    private final long selectTimeout;
    private final SSLContext sslContext;
    private final int concurrency;
    private final ExecutorService executor;
    private final RunnableNioProcessor[] processors;
    private final NioBalancer balancer;

    private NioClient(NioBuilder<?> builder) {
        super(builder);
        try {
            this.selectTimeout = builder.selectTimeout;
            this.sslContext = builder.sslContext;
            this.concurrency = builder.concurrency;
            this.executor = Executors.newFixedThreadPool(concurrency);
            this.processors = new RunnableNioProcessor[concurrency];
            for (int i = 0; i < concurrency; i++) executor.execute(processors[i] = new RunnableNioProcessor());
            this.balancer = builder.balancer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        this.executor.shutdown();
    }

    private class RunnableNioProcessor implements NioProcessor, NioCalls.NioConsumer, Runnable, Closeable {
        private final TimeoutManager timeoutManager;
        private final Selector selector;
        private final ByteBuffer buffer;
        private final NioCalls calls;

        public RunnableNioProcessor() throws IOException {
            this.timeoutManager = new SortedTimeoutManager();
            this.selector = Selector.open();
            this.buffer = ByteBuffer.allocate(4096);
            this.calls = new NioCalls(selector);
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
        public void process(SocketAddress address, Action action) {
            calls.offer(address, action);
        }

        private void doKeyCancel(SelectionKey key) {
            key.cancel();
            IOKit.close(key.channel());
        }

        private void doExceptionCatch(SelectionKey key, StatusException statusException) {
            try {
                Action action = (Action) key.attachment();
                for (Catcher catcher : catchers.values()) {
                    if (catcher instanceof NioCatcher && catcher.catchable(statusException)) {
                        ((NioCatcher) catcher).nioCatched(NioClient.this, action, statusException);
                        break;
                    }
                }
            } catch (Exception e) {
                doChannelException(key, e);
            }
        }

        private void doChannelException(SelectionKey key, Exception e) {
            try {
                Action action = (Action) key.attachment();
                NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                listener.onException(action, e);
            } catch (RuntimeException re) {
                logger.warn("", re);
            }
        }

        private void doChannelRecv(SelectionKey key) throws Exception {
            SocketChannel channel = (SocketChannel) key.channel();
            Action action = (Action) key.attachment();
            NioResponse response = (NioResponse) action.getExtra().get(NioResponse.class);
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            boolean finished = response.load(buffer);
            if (finished) {
                doKeyCancel(key);

                NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                listener.onCompleted(action);
            }
        }

        private void doChannelSend(SelectionKey key) throws Exception {
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
                timeoutManager.addRecvTimeoutHandler(key, request.getReadTimeout());

                NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                listener.onRequested(action);
            }
        }

        private void doChannelConn(SelectionKey key) throws Exception {
            SocketChannel channel = (SocketChannel) key.channel();
            Action action = (Action) key.attachment();
            NioRequest request = (NioRequest) action.getExtra().get(NioRequest.class);
            if (channel.isConnectionPending() && channel.finishConnect()) {
                // 本来HTTP 模式情况下这里只需要注册WRITE即可 但是为了适配SSL模式的握手过程的握手数据读写 这里必须注册成WRITE | READ 但是只计算Write Timeout
                channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, action);
                timeoutManager.addSendTimeoutHandler(key, request.getWriteTimeout());

                NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
                listener.onConnected(action);
            }
        }

        @Override
        public void run() {
            // 运行
            while (!isDestroyed() && selector.isOpen()) {
                SelectionKey key = null;
                try {
                    // 处理超时
                    timeoutManager.fire();
                    // 处理注册
                    calls.foreach(this);
                    // 最多等待 selected timeout 时间进行一次超时检查
                    if (selector.select(selectTimeout) == 0) continue;
                    // 如果客户端被摧毁
                    if (isDestroyed() || !selector.isOpen()) break;
                    // 迭代处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        if (!key.isValid()) continue;
                        if (key.isConnectable()) doChannelConn(key);
                        if (key.isWritable()) doChannelSend(key);
                        if (key.isReadable()) doChannelRecv(key);
                    }
                    // 异常捕获
                } catch (StatusException e) {
                    if (key != null && key.isValid()) doKeyCancel(key);
                    if (key != null) doExceptionCatch(key, e);
                    else logger.warn("unexpected exception", e);
                } catch (Exception e) {
                    if (key != null && key.isValid()) doKeyCancel(key);
                    if (key != null) doChannelException(key, e);
                    else logger.warn("unexpected exception", e);
                }
            }
            IOKit.close(this);
        }

        @Override
        public synchronized void close() throws IOException {
            if (selector.isOpen()) selector.close();
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
        balancer.dispatch(address, action, this, processors);
        return null;
    }

    @Override
    protected Object doSchedule(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        for (Scheduler scheduler : schedulers.values()) {
            if (scheduler instanceof NioScheduler && scheduler.supports(action)) {
                action.getExtra().put(Scheduler.class, scheduler);
                Type type = scheduler.getBodyType(this, action);
                body.setType(type);
                Object value = ((NioScheduler) scheduler).doMotivateSchedule(this, action);
                result.setValue(value);
                return value;
            }
        }
        throw new UnsupportedOperationException();
    }

    protected Request newRequest(Action action) throws Exception {
        NioRequest request = nioConnect(action, gateway, this).getRequest();
        action.getExtra().put(NioRequest.class, request);
        return request;
    }

    protected Response newResponse(Action action) throws Exception {
        NioResponse response = nioConnect(action, gateway, this).getResponse();
        action.getExtra().put(NioResponse.class, response);
        return response;
    }

    public NioCreator<?> creator() {
        return new NioCreator();
    }

    public class NioCreator<C extends NioCreator<C>> extends Client.Creator<C> {
        @Override
        public <T> T create(Class<T> interfase, URL endpoint) {
            String protocol = endpoint.getProtocol();
            String host = endpoint.getHost();
            Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
            String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
            return new JestfulNioInvocationHandler<T>(interfase, protocol, host, port, route, NioClient.this, load(forePlugins), load(backPlugins)).getProxy();
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

    public static NioBuilder<?> builder() {
        return new NioBuilder();
    }

    public static class NioBuilder<B extends NioBuilder<B>> extends Client.Builder<B> {
        private long selectTimeout = 1000L;
        private SSLContext sslContext;
        private int concurrency = Runtime.getRuntime().availableProcessors() * 2;
        private NioBalancer balancer = new RandomNioBalancer();

        public NioBuilder() {
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

        public B setSelectTimeout(long selectTimeout) {
            if (selectTimeout < 0) {
                throw new IllegalArgumentException("selected timeout is negative");
            }
            this.selectTimeout = selectTimeout;
            return (B) this;
        }

        public B setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            if (sslContext == null) {
                throw new IllegalArgumentException("SSLContext can not be null");
            }
            return (B) this;
        }

        public B setConcurrency(int concurrency) {
            if (concurrency < 1) {
                throw new IllegalArgumentException("concurrency can not lesser than 1");
            }
            this.concurrency = concurrency;
            return (B) this;
        }

        public B setBalancer(NioBalancer balancer) {
            if (balancer == null) {
                throw new IllegalArgumentException("balancer can not be null");
            }
            this.balancer = balancer;
            return (B) this;
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
                throw new UnexpectedStatusException(action.getURI(), action.getRestful().getMethod(), status, body);
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

    public SSLContext getSslContext() {
        return sslContext;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public NioBalancer getBalancer() {
        return balancer;
    }
}
