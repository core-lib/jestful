package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpsConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpsNioConnector extends HttpsConnector implements NioConnector {

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        NioRequest request = new JestfulNioSSLClientRequest(action, this, gateway, 0, 0, 0, null);
        NioResponse response = new JestfulNioSSLClientResponse(action, this, gateway, null);
        NioConnection connection = new NioConnection(request, response);
        return connection;
    }

}
