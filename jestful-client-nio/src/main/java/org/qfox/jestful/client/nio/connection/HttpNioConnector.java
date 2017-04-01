package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpNioConnector extends HttpConnector implements NioConnector {

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        NioRequest request = new JestfulNioHttpClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
        NioResponse response = new JestfulNioHttpClientResponse(action, this, gateway);
        NioConnection connection = new NioConnection(request, response);
        return connection;
    }

}
