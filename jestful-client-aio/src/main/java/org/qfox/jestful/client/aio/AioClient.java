package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.aio.connection.AioConnector;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.connection.KeepAlive;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
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
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
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
    private final ExecutorService cpu;
    private final AsynchronousChannelGroup aioChannelGroup;
    private final SSLContext sslContext;
    private final AioOptions options;

    private AioClient(AioBuilder<?> builder) throws IOException {
        super(builder);
        this.concurrency = builder.concurrency;
        this.cpu = concurrency > 0 ? Executors.newFixedThreadPool(concurrency) : Executors.newCachedThreadPool();
        this.aioChannelGroup = AsynchronousChannelGroup.withThreadPool(cpu);
        this.sslContext = builder.sslContext;
        this.options = builder.options;
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
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open(aioChannelGroup);
        PrepareCompletionHandler handler = new PrepareCompletionHandler(this, channel, action);
        cpu.execute(handler);
        return promise;
    }

    protected Request newRequest(Action action) throws Exception {
        AioRequest request = aioConnect(action, gateway, this).getRequest();
        request.setRequestHeader("User-Agent", userAgent);
        action.getExtra().put(AioRequest.class, request);
        return request;
    }

    protected Response newResponse(Action action) throws Exception {
        AioResponse response = aioConnect(action, gateway, this).getResponse();
        action.getExtra().put(AioResponse.class, response);
        return response;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (aioChannelGroup != null) aioChannelGroup.shutdown();
        if (cpu != null) cpu.shutdown();
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
                connection = aioConnector.aioConnect(action, gateway, this);
                action.getExtra().put(AioConnection.class, connection);
                // HTTP/1.1 要求不支持 Keep-Alive 的客户端必须在请求头声明 Connection: close 否则访问Github这样的网站就会有非常严重的性能问题
                (keepAlive == KeepAlive.DEFAULT ? KeepAlive.OFF : keepAlive).config(connection);
                return connection;
            }
        }
        throw new IOException("unsupported protocol " + action.getProtocol());
    }

    public int getConcurrency() {
        return concurrency;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public AioOptions getOptions() {
        return options;
    }

    public static class AioBuilder<B extends AioBuilder<B>> extends Client.Builder<B> {
        private int concurrency = Runtime.getRuntime().availableProcessors();
        private SSLContext sslContext;
        private AioOptions options = AioOptions.DEFAULT;

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

        // 暂时不支持 AIO 的长连接
        @Override
        public B setKeepAlive(KeepAlive keepAlive) {
            return (B) this;
        }
    }

    private class AioPromise extends BioPromise {
        private Set<Callback<Object>> callbacks;

        AioPromise(Action action) {
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
                        if (callbacks == null) callbacks = new HashSet<>();
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
