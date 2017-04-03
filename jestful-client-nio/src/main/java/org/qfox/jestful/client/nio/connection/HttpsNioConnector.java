package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpsConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 * Version: 1.0
 */
public class HttpsNioConnector extends HttpsConnector implements NioConnector {
    private final Object lock = new Object();
    private SSLContext context;

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        SSLContext sslContext = client.getSslContext();
        sslContext = sslContext != null ? sslContext : getDefaultSSLContext();
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(true);
        engine.beginHandshake();
        NioSSLChannel nioSSLChannel = new JestfulNioSSLChannel(engine);
        NioRequest request = new JestfulNioHttpsClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout(), nioSSLChannel);
        NioResponse response = new JestfulNioHttpsClientResponse(action, this, gateway, nioSSLChannel);
        return new NioConnection(request, response);
    }

    private SSLContext getDefaultSSLContext() throws IOException {
        if (context != null) return context;
        synchronized (lock) {
            if (context != null) return context;
            try {
                context = SSLContext.getInstance("SSL");
                context.init(null, null, null);
                return context;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

}
