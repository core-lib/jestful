package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.aio.connection.AioConnector;
import org.qfox.jestful.client.aio.pool.AioConnectionKey;
import org.qfox.jestful.client.aio.pool.AioConnectionPool;
import org.qfox.jestful.client.aio.pool.ConcurrentAioConnectionPool;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.exception.UnsupportedProtocolException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class AioClient extends Client implements AioConnector {
    private static AioClient defaultClient;
    private final int concurrency;
    private final int bufferSize;
    private final ExecutorService cpu;
    private final AsynchronousChannelGroup aioChannelGroup;
    private final SSLContext sslContext;
    private final AioOptions options;
    private final AioConnectionPool connectionPool;

    private AioClient(AioBuilder<?> builder) throws IOException {
        super(builder);
        this.concurrency = builder.concurrency;
        this.bufferSize = builder.bufferSize;
        this.cpu = concurrency > 0 ? Executors.newFixedThreadPool(concurrency) : Executors.newCachedThreadPool();
        this.aioChannelGroup = AsynchronousChannelGroup.withThreadPool(cpu);
        this.sslContext = builder.sslContext;
        this.options = builder.options;
        this.connectionPool = builder.connectionPool;
    }

    public static AioClient getDefaultClient() {
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

    public static AioBuilder<?> builder() {
        return new AioBuilder();
    }

    public Object react(Action action) throws Exception {
        AioPromise promise = new AioPromise(action);
        AioEventListener listener = new JestfulAioEventListener(promise);
        action.getExtra().put(AioEventListener.class, listener);
        return promise;
    }

    protected AioRequest newRequest(Action action) throws Exception {
        AioRequest request = aioConnect(action, gateway, this).getRequest();
        request.setRequestHeader("User-Agent", userAgent);
        return request;
    }

    protected AioResponse newResponse(Action action) throws Exception {
        return aioConnect(action, gateway, this).getResponse();
    }

    @Override
    public synchronized void destroy() {
        if (destroyed) return;
        super.destroy();
        if (aioChannelGroup != null) aioChannelGroup.shutdown();
        if (cpu != null) cpu.shutdown();
        if (connectionPool != null) connectionPool.destroy();
    }

    @Override
    public AioCreator<?> creator() {
        return new AioCreator();
    }

    @Override
    public AioConnection aioConnect(Action action, Gateway gateway, AioClient client) throws IOException {
        AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
        if (connection != null) {
            return connection;
        }

        for (Connector connector : connectors.values()) {
            if (connector instanceof AioConnector && connector.supports(action)) {
                AioConnector aioConnector = (AioConnector) connector;
                SocketAddress address = aioConnector.aioAddress(action, gateway, this);
                AioConnectionKey connectionKey = new AioConnectionKey(address, aioConnector, action, gateway, client);
                connection = connectionPool.acquire(connectionKey);
                connection.reset(action, gateway, this);
                if (keepAlive != null) connection.setKeepAlive(keepAlive);
                if (idleTimeout != null && idleTimeout >= 0) connection.setIdleTimeout(idleTimeout);
                action.getExtra().put(AioConnection.class, connection);
                return connection;
            }
        }
        throw new UnsupportedProtocolException(action.getProtocol());
    }

    @Override
    public SocketAddress aioAddress(Action action, Gateway gateway, AioClient client) throws IOException {
        AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
        if (connection != null) {
            return connection.getAddress();
        }
        for (Connector connector : connectors.values()) {
            if (connector instanceof AioConnector && connector.supports(action)) {
                AioConnector aioConnector = (AioConnector) connector;
                return aioConnector.aioAddress(action, gateway, this);
            }
        }
        throw new UnsupportedProtocolException(action.getProtocol());
    }

    public int getConcurrency() {
        return concurrency;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public AsynchronousChannelGroup getAioChannelGroup() {
        return aioChannelGroup;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public AioOptions getOptions() {
        return options;
    }

    public AioConnectionPool getConnectionPool() {
        return connectionPool;
    }

    private enum State {
        STANDING, HANDLED
    }

    public static class AioBuilder<B extends AioBuilder<B>> extends Client.Builder<B> {
        private int concurrency = Runtime.getRuntime().availableProcessors();
        private int bufferSize = 4096;
        private SSLContext sslContext;
        private AioOptions options = AioOptions.DEFAULT;
        private AioConnectionPool connectionPool = new ConcurrentAioConnectionPool();

        AioBuilder() {
            this.connTimeout = 20 * 1000;
            this.readTimeout = Integer.MAX_VALUE;
            this.writeTimeout = Integer.MAX_VALUE;
            this.userAgent = "Mozilla/5.0"
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
        }

        @Override
        public AioClient build() {
            try {
                return new AioClient(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public B setConcurrency(int concurrency) {
            if (concurrency < 0) {
                throw new IllegalArgumentException("concurrency can not be negative");
            }
            this.concurrency = concurrency;
            return (B) this;
        }

        public B setBufferSize(int bufferSize) {
            if (bufferSize <= 0) {
                throw new IllegalArgumentException("buffer size must bigger than zero");
            }
            this.bufferSize = bufferSize;
            return (B) this;
        }

        public B setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            if (sslContext == null) {
                throw new IllegalArgumentException("SSLContext can not be null");
            }
            return (B) this;
        }

        public B setOptions(AioOptions options) {
            if (options == null) {
                throw new IllegalArgumentException("options can not be null");
            }
            this.options = options;
            return (B) this;
        }

        public B setConnectionPool(AioConnectionPool connectionPool) {
            if (options == null) {
                throw new IllegalArgumentException("connection pool can not be null");
            }
            this.connectionPool = connectionPool;
            return (B) this;
        }
    }

    private class AioPromise extends BioPromise {
        private volatile Set<Callback<Object>> callbacks;
        private volatile State state = State.STANDING;

        AioPromise(Action action) {
            super(action);
        }

        @Override
        public Object acquire() throws Exception {
            if (success == null) {
                synchronized (lock) {
                    if (canceled) throw new IllegalStateException("canceled");
                    if (state == State.STANDING) {
                        AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
                        PrepareCompletionHandler handler = new PrepareCompletionHandler(AioClient.this, connection, action);
                        cpu.execute(handler);
                        state = State.HANDLED;
                    }
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
                        if (callbacks == null) callbacks = new HashSet<>();
                        callbacks.add(callback);
                        if (state == State.STANDING) {
                            AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
                            PrepareCompletionHandler handler = new PrepareCompletionHandler(AioClient.this, connection, action);
                            cpu.execute(handler);
                            state = State.HANDLED;
                        }
                    } else {
                        super.accept(callback);
                    }
                }
            } else {
                super.accept(callback);
            }
        }

        @Override
        public void cancel() {
            if (canceled) return;
            synchronized (lock) {
                if (canceled) return;
                canceled = true;
                lock.notifyAll();
                // 回收连接
                AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
                if (connection.isKeepAlive()) {
                    SocketAddress address = connection.getAddress();
                    AioConnectionKey connectionKey = new AioConnectionKey(address);
                    connectionPool.release(connectionKey, connection);
                } else {
                    IOKit.close(connection);
                }
            }
        }

        @Override
        public Client client() {
            return AioClient.this;
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

    public class AioCreator<C extends AioCreator<C>> extends Client.Creator<C> {
        @Override
        public <T> T create(Class<T> interfase, URL endpoint) {
            String protocol = endpoint.getProtocol();
            String host = endpoint.getHost();
            Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
            String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
            return new JestfulAioInvocationHandler<>(interfase, protocol, host, port, route, AioClient.this, forePlugins, backPlugins).getProxy();
        }
    }

    private class JestfulAioEventListener extends AioEventAdapter {
        private final AioPromise promise;

        JestfulAioEventListener(AioPromise promise) {
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
                Map<String, String[]> header = new CaseInsensitiveMap<>();
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
                Map<String, String> header = new CaseInsensitiveMap<>();
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
}
