package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpNioConnector extends HttpConnector implements NioConnector {

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        SocketAddress address = nioAddress(action, gateway, client);
        return new HttpNioConnection(this, address, action, gateway, client);
    }

    @Override
    public SocketAddress nioAddress(Action action, Gateway gateway, NioClient client) throws IOException {
        return address(action, gateway, client);
    }

}
