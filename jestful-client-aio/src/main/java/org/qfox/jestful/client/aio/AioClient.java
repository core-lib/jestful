package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.aio.connection.AioConnector;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class AioClient extends Client implements AioConnector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static AioClient defaultClient;
    private final int concurrency;
    private final ExecutorService cpu;
    private final AsynchronousChannelGroup aioChannelGroup;
    private final SSLContext sslContext;

    private AioClient(AioBuilder<?> builder) throws IOException {
        super(builder);
        this.concurrency = builder.concurrency;
        this.cpu = concurrency > 0 ? Executors.newFixedThreadPool(concurrency) : Executors.newCachedThreadPool();
        this.aioChannelGroup = AsynchronousChannelGroup.withThreadPool(cpu);
        this.sslContext = builder.sslContext;
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

    private class AioPromise implements Promise {
        private final Object lock = new Object();

        private final Action action;

        private volatile Boolean success;
        private volatile Object result;
        private volatile Exception exception;

        private Map<Object, Integer> listeners = new LinkedHashMap<Object, Integer>();

        AioPromise(Action action) {
            this.action = action;
        }

        @Override
        public Object get() throws Exception {
            if (success == null) {
                synchronized (lock) {
                    if (success == null) lock.wait();
                    return get();
                }
            } else if (success) {
                return result;
            } else {
                throw exception;
            }
        }

        @Override
        public void get(Callback<Object> callback) {
            put(callback, 0);
        }

        @Override
        public void get(OnCompleted<Object> onCompleted) {
            put(onCompleted, 1);
        }

        @Override
        public void get(OnSuccess<Object> onSuccess) {
            put(onSuccess, 2);
        }

        @Override
        public void get(OnFail onFail) {
            put(onFail, 3);
        }

        void put(final Object listener, final Integer type) {
            synchronized (lock) {
                if (success == null) listeners.put(listener, type);
                else executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        call(listener, type);
                    }
                });
            }
        }

        void call(Object listener, Integer type) {
            switch (type != null ? type : -1) {
                case 0:
                    callback((Callback<Object>) listener);
                    break;
                case 1:
                    onCompleted((OnCompleted<Object>) listener);
                    break;
                case 2:
                    onSuccess((OnSuccess<Object>) listener);
                    break;
                case 3:
                    onFail((OnFail) listener);
                    break;
                default:
                    break;
            }
        }

        void callback(Callback<Object> callback) {
            Object result = null;
            Throwable throwable = null;
            try {
                callback.onSuccess(result = get());
            } catch (Throwable e) {
                callback.onFail(throwable = e);
            } finally {
                callback.onCompleted(throwable == null, result, throwable);
            }
        }

        void onCompleted(OnCompleted<Object> onCompleted) {
            Object result = null;
            Throwable throwable = null;
            try {
                result = get();
            } catch (Throwable e) {
                throwable = e;
            } finally {
                onCompleted.call(throwable == null, result, throwable);
            }
        }

        void onSuccess(OnSuccess<Object> onSuccess) {
            try {
                onSuccess.call(get());
            } catch (Throwable e) {
                logger.error("", e);
            }
        }

        void onFail(OnFail onFail) {
            try {
                get();
            } catch (Throwable e) {
                onFail.call(e);
            }
        }

        void fulfill() {
            synchronized (lock) {
                result = action.getResult().getBody().getValue();
                exception = action.getResult().getException();
                success = exception == null;
                lock.notifyAll();
                for (final Map.Entry<Object, Integer> entry : listeners.entrySet()) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            call(entry.getKey(), entry.getValue());
                        }
                    });
                }
            }
        }

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
                return connection;
            }
        }
        throw new IOException("unsupported protocol " + action.getProtocol());
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

    public static class AioBuilder<B extends AioBuilder<B>> extends Client.Builder<B> {
        private int concurrency = Runtime.getRuntime().availableProcessors();
        private SSLContext sslContext;

        public AioBuilder() {
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

    public int getConcurrency() {
        return concurrency;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
