package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class AioClient extends Client {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static AioClient defaultClient;

    private final ExecutorService executor;
    private final AsynchronousChannelGroup aioChannelGroup;

    private AioClient(Builder<?> builder) {
        super(builder);
        try {
            this.executor = builder.concurrent > 0 ? Executors.newFixedThreadPool(builder.concurrent) : Executors.newCachedThreadPool();
            this.aioChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object react(Action action) throws Exception {
        String protocol = action.getProtocol();
        String host = action.getHost();
        Integer port = action.getPort();
        port = port != null && port >= 0 ? port : "https".equalsIgnoreCase(protocol) ? 443 : "http".equalsIgnoreCase(protocol) ? 80 : 0;
        
        return null;
    }

    @Override
    public <T> T create(Class<T> interfase, URL endpoint) {
        String protocol = endpoint.getProtocol();
        String host = endpoint.getHost();
        Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
        String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
        return new JestfulAioInvocationHandler<T>(interfase, protocol, host, port, route, this).getProxy();
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
    }

}
