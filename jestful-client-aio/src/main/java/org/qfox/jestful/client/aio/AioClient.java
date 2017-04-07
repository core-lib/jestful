package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.aio.connection.AioConnector;
import org.qfox.jestful.client.aio.scheduler.AioScheduler;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class AioClient extends Client implements AioConnector {
    private static AioClient defaultClient;

    private final ExecutorService executor;
    private final AsynchronousChannelGroup aioChannelGroup;
    private final SSLContext sslContext;

    private AioClient(Builder<?> builder) {
        super(builder);
        try {
            this.executor = builder.concurrent > 0 ? Executors.newFixedThreadPool(builder.concurrent) : Executors.newCachedThreadPool();
            this.aioChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            this.sslContext = builder.sslContext;
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
        AioEventListener listener = new JestfulAioEventListener();
        action.getExtra().put(AioEventListener.class, listener);
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open(aioChannelGroup);
        channel.connect(address, action, new ConnectCompletionHandler(channel));
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (aioChannelGroup != null) aioChannelGroup.shutdown();
        if (executor != null) executor.shutdown();
    }

    @Override
    public <T> T create(Class<T> interfase, URL endpoint) {
        String protocol = endpoint.getProtocol();
        String host = endpoint.getHost();
        Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
        String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
        return new JestfulAioInvocationHandler<T>(interfase, protocol, host, port, route, this).getProxy();
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

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> extends org.qfox.jestful.client.Client.Builder<T> {
        private int concurrent = Runtime.getRuntime().availableProcessors() * 2;
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
        public AioClient build() {
            return new AioClient(this);
        }

        public T setConcurrent(int concurrent) {
            if (concurrent < 0) {
                throw new IllegalArgumentException("concurrent can not be negative");
            }
            this.concurrent = concurrent;
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

    private class JestfulAioEventListener extends AioEventAdapter {
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

            AioScheduler scheduler = (AioScheduler) action.getExtra().get(Scheduler.class);
            scheduler.doCallbackSchedule(AioClient.this, action);
        }

        @Override
        public void onException(Action action, Exception exception) {
            Request request = action.getRequest();
            Response response = action.getResponse();
            IOKit.close(request);
            IOKit.close(response);

            try {
                action.getResult().setException(exception);

                AioScheduler scheduler = (AioScheduler) action.getExtra().get(Scheduler.class);
                scheduler.doCallbackSchedule(AioClient.this, action);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
