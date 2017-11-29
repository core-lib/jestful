package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

public class HttpNioConnection extends NioConnection {

    public HttpNioConnection(NioConnector connector, SocketAddress address, Action action, Gateway gateway, NioClient client) throws IOException {
        super(connector, address, action, gateway, client);
    }

    @Override
    public void reset(Action action, Gateway gateway, NioClient client) {
        super.reset(action, gateway, client);
        request = new JestfulNioHttpClientRequest(action, connector, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
        response = new JestfulNioHttpClientResponse(action, connector, gateway);
    }
}
