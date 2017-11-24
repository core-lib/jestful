package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.balancer.LoopedNioBalancer;
import org.qfox.jestful.client.nio.balancer.NioBalancer;
import org.qfox.jestful.client.nio.catcher.NioCatcher;
import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.client.nio.connection.NioConnector;
import org.qfox.jestful.client.nio.timeout.SortedTimeoutManager;
import org.qfox.jestful.client.nio.timeout.TimeoutManager;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
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
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class NioClient extends Client implements NioConnector {
    private static NioClient defaultClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final long selectTimeout;
    private final SSLContext sslContext;
    private final int concurrency;
    private final ExecutorService cpu;
    private final NioProcessor[] processors;
    private final NioBalancer balancer;
    private final Boolean so_broadcast;
    private final Boolean so_keepalive;
    private final Integer so_sndbuf;
    private final Integer so_rcvbuf;
    private final Boolean so_reuseaddr;
    private final Integer so_linger;
    private final Integer ip_tos;
    private final Integer ip_multicast_ttl;
    private final Boolean ip_multicast_loop;
    private final Boolean tcp_nodelay;
    private NetworkInterface ip_multicast_if;

    private NioClient(NioBuilder<?> builder) throws IOException {
        super(builder);
        this.selectTimeout = builder.selectTimeout;
        this.sslContext = builder.sslContext;
        this.concurrency = builder.concurrency;
        this.cpu = Executors.newFixedThreadPool(concurrency);
        this.processors = new NioKernel[concurrency];
        for (int i = 0; i < concurrency; i++) cpu.execute(processors[i] = new NioKernel());
        this.balancer = builder.balancer;

        this.so_broadcast = builder.so_broadcast;
        this.so_keepalive = builder.so_keepalive;
        this.so_sndbuf = builder.so_sndbuf;
        this.so_rcvbuf = builder.so_rcvbuf;
        this.so_reuseaddr = builder.so_reuseaddr;
        this.so_linger = builder.so_linger;
        this.ip_tos = builder.ip_tos;
        this.ip_multicast_if = builder.ip_multicast_if;
        this.ip_multicast_ttl = builder.ip_multicast_ttl;
        this.ip_multicast_loop = builder.ip_multicast_loop;
        this.tcp_nodelay = builder.tcp_nodelay;
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

    @Override
    public void destroy() {
        super.destroy();
        this.cpu.shutdown();
    }

    public Object react(Action action) throws Exception {
        NioPromise promise = new NioPromise(action);
        NioEventListener listener = new JestfulNioEventListener(promise);
        action.getExtra().put(NioEventListener.class, listener);
        balancer.dispatch(action, this, processors);
        return promise;
    }

    protected Request newRequest(Action action) throws Exception {
        NioRequest request = nioConnect(action, gateway, this).getRequest();
        request.setRequestHeader("User-Agent", userAgent);
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
                keepAlive.config(connection);
                return connection;
            }
        }
        throw new IOException("unsupported protocol " + action.getProtocol());
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

    public ExecutorService getCpu() {
        return cpu;
    }

    public NioBalancer getBalancer() {
        return balancer;
    }

    public Boolean getSo_broadcast() {
        return so_broadcast;
    }

    public Boolean getSo_keepalive() {
        return so_keepalive;
    }

    private class NioPromise extends BioPromise {
        private Set<Callback<Object>> callbacks;

        NioPromise(Action action) {
            super(action);
        }

        @Override
        public Object acquire() throws Exception {
            if (success == null) {
                synchronized (lock) {
                    if (success == null) lock.wait();
                    return acquire();
                }
            } else if (success) {
                return result;
            } else {
                throw exception;
            }
        }

        @Override
        public void accept(Callback<Object> callback) {
            if (success == null) {
                synchronized (lock) {
                    if (success == null) {
                        if (callbacks == null) callbacks = new HashSet<Callback<Object>>();
                        callbacks.add(callback);
                    } else {
                        super.accept(callback);
                    }
                }
            } else {
                super.accept(callback);
            }
        }

        @Override
        public Client client() {
            return NioClient.this;
        }

        void fulfill() {
            synchronized (lock) {
                result = action.getResult().getBody().getValue();
                exception = action.getResult().getException();
                success = exception == null;
                lock.notifyAll();
                if (callbacks != null) for (Callback<Object> callback : callbacks) super.accept(callback);
            }
        }

    }

    public class NioCreator<C extends NioCreator<C>> extends Client.Creator<C> {
        @Override
        public <T> T create(Class<T> interfase, URL endpoint) {
            String protocol = endpoint.getProtocol();
            String host = endpoint.getHost();
            Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
            String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
            return new JestfulNioInvocationHandler<T>(interfase, protocol, host, port, route, NioClient.this, forePlugins, backPlugins).getProxy();
        }
    }

    private class JestfulNioEventListener extends NioEventAdapter {
        private final NioPromise promise;

        JestfulNioEventListener(NioPromise promise) {
            this.promise = promise;
        }

        @Override
        public void onConnected(Action action) throws Exception {
            Request request = action.getRequest();
            Restful restful = action.getRestful();

            if (restful.isAcceptBody()) serialize(action);
            else request.connect();
        }

        @Override
        public void onRequested(Action action) throws Exception {
            Response response = action.getResponse();
            if (!response.isResponseSuccess()) {
                String contentType = response.getContentType();
                MediaType mediaType = MediaType.valueOf(contentType);
                String charset = mediaType.getCharset();
                if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
                if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
                if (StringKit.isBlank(charset)) charset = java.nio.charset.Charset.defaultCharset().name();
                Status status = response.getResponseStatus();
                Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
                String[] keys = response.getHeaderKeys();
                for (String key : keys) header.put(key == null ? "" : key, response.getResponseHeaders(key));
                InputStream in = response.getResponseInputStream();
                InputStreamReader reader = in == null ? null : new InputStreamReader(in, charset);
                String body = reader != null ? IOKit.toString(reader) : "";
                throw new UnexpectedStatusException(action.getURI(), action.getRestful().getMethod(), status, header, body);
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
                for (String key : response.getHeaderKeys()) header.put(key != null ? key : "", response.getResponseHeader(key));
                action.getResult().getBody().setValue(header);
            }

            promise.fulfill();
        }

        @Override
        public void onException(Action action, Exception exception) {
            Request request = action.getRequest();
            Response response = action.getResponse();
            IOKit.close(request);
            IOKit.close(response);

            try {
                action.getResult().setException(exception);

                promise.fulfill();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Integer getSo_sndbuf() {
        return so_sndbuf;
    }

    public Integer getSo_rcvbuf() {
        return so_rcvbuf;
    }

    public Boolean getSo_reuseaddr() {
        return so_reuseaddr;
    }

    public Integer getSo_linger() {
        return so_linger;
    }

    public Integer getIp_tos() {
        return ip_tos;
    }

    public NetworkInterface getIp_multicast_if() {
        return ip_multicast_if;
    }

    public void setIp_multicast_if(NetworkInterface ip_multicast_if) {
        this.ip_multicast_if = ip_multicast_if;
    }

    public Integer getIp_multicast_ttl() {
        return ip_multicast_ttl;
    }

    public Boolean getIp_multicast_loop() {
        return ip_multicast_loop;
    }

    public Boolean getTcp_nodelay() {
        return tcp_nodelay;
    }

    public static class NioBuilder<B extends NioBuilder<B>> extends Client.Builder<B> {
        private long selectTimeout = 1000L;
        private SSLContext sslContext;
        private int concurrency = Runtime.getRuntime().availableProcessors();
        private NioBalancer balancer = new LoopedNioBalancer();
        private Boolean so_broadcast;
        private Boolean so_keepalive;
        private Integer so_sndbuf;
        private Integer so_rcvbuf;
        private Boolean so_reuseaddr;
        private Integer so_linger;
        private Integer ip_tos;
        private NetworkInterface ip_multicast_if;
        private Integer ip_multicast_ttl;
        private Boolean ip_multicast_loop;
        private Boolean tcp_nodelay;

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
            try {
                return new NioClient(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        public B setSo_broadcast(boolean so_broadcast) {
            this.so_broadcast = so_broadcast;
            return (B) this;
        }

        public B setSo_keepalive(boolean so_keepalive) {
            this.so_keepalive = so_keepalive;
            return (B) this;
        }

        public B setSo_sndbuf(int so_sndbuf) {
            this.so_sndbuf = so_sndbuf;
            return (B) this;
        }

        public B setSo_rcvbuf(int so_rcvbuf) {
            this.so_rcvbuf = so_rcvbuf;
            return (B) this;
        }

        public B setSo_reuseaddr(boolean so_reuseaddr) {
            this.so_reuseaddr = so_reuseaddr;
            return (B) this;
        }

        public B setSo_linger(int so_linger) {
            this.so_linger = so_linger;
            return (B) this;
        }

        public B setIp_tos(int ip_tos) {
            this.ip_tos = ip_tos;
            return (B) this;
        }

        public B setIp_multicast_if(NetworkInterface ip_multicast_if) {
            this.ip_multicast_if = ip_multicast_if;
            return (B) this;
        }

        public B setIp_multicast_ttl(int ip_multicast_ttl) {
            this.ip_multicast_ttl = ip_multicast_ttl;
            return (B) this;
        }

        public B setIp_multicast_loop(boolean ip_multicast_loop) {
            this.ip_multicast_loop = ip_multicast_loop;
            return (B) this;
        }

        public B setTcp_nodelay(boolean tcp_nodelay) {
            this.tcp_nodelay = tcp_nodelay;
            return (B) this;
        }
    }

    private class NioKernel implements NioProcessor, NioCalls.NioConsumer, Closeable {
        private final TimeoutManager timeoutManager;
        private final Selector selector;
        private final ByteBuffer buffer;
        private final NioCalls calls;

        NioKernel() throws IOException {
            this.timeoutManager = new SortedTimeoutManager();
            this.selector = Selector.open();
            this.buffer = ByteBuffer.allocate(4096);
            this.calls = new NioCalls(selector);
        }

        @Override
        public void consume(Action action) {
            try {
                String protocol = action.getProtocol();
                String host = action.getHostname();
                Integer port = action.getPort();
                port = port != null && port >= 0 ? port : "https".equalsIgnoreCase(protocol) ? 443 : "http".equalsIgnoreCase(protocol) ? 80 : 0;
                Gateway gateway = NioClient.this.getGateway();
                SocketAddress address = gateway != null && gateway.isProxy() ? gateway.toSocketAddress() : new InetSocketAddress(host, port);
                (gateway != null ? gateway : Gateway.NULL).onConnected(action);

                SocketChannel channel = SocketChannel.open();
                channel.configureBlocking(false);
                doChannelConf(channel);
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
        public void process(Action action) {
            calls.offer(action);
        }

        @Override
        public int tasks() {
            return selector.keys().size();
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
                        ((NioCatcher) catcher).nioCaught(NioClient.this, action, statusException);
                        return;
                    }
                }
                throw statusException;
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

        private void doChannelConf(SocketChannel channel) throws IOException {
            if (so_broadcast != null) channel.setOption(StandardSocketOptions.SO_BROADCAST, so_broadcast);
            if (so_keepalive != null) channel.setOption(StandardSocketOptions.SO_KEEPALIVE, so_keepalive);
            if (so_sndbuf != null) channel.setOption(StandardSocketOptions.SO_SNDBUF, so_sndbuf);
            if (so_rcvbuf != null) channel.setOption(StandardSocketOptions.SO_RCVBUF, so_rcvbuf);
            if (so_reuseaddr != null) channel.setOption(StandardSocketOptions.SO_REUSEADDR, so_reuseaddr);
            if (so_linger != null) channel.setOption(StandardSocketOptions.SO_LINGER, so_linger);
            if (ip_tos != null) channel.setOption(StandardSocketOptions.IP_TOS, ip_tos);
            if (ip_multicast_if != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ip_multicast_if);
            if (ip_multicast_ttl != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, ip_multicast_ttl);
            if (ip_multicast_loop != null) channel.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, ip_multicast_loop);
            if (tcp_nodelay != null) channel.setOption(StandardSocketOptions.TCP_NODELAY, tcp_nodelay);
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
}
