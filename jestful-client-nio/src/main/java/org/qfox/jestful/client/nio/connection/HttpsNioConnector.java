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
 */
public class HttpsNioConnector extends HttpsConnector implements NioConnector {
    private final Object lock = new Object();
    private SSLContext context;

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        SSLContext sslContext = client.getSslContext();
        if (sslContext == null) {
            if (context != null) sslContext = context;
            synchronized (lock) {
                if (context != null) sslContext = context;
            }
        }
        SSLEngine engine = sslContext.createSSLEngine();
        NioRequest request = new JestfulNioHttpsClientRequest(action, this, gateway, 0, 0, 0, engine);
        NioResponse response = new JestfulNioHttpsClientResponse(action, this, gateway, engine);
        NioConnection connection = new NioConnection(request, response);
        return connection;
    }

}
