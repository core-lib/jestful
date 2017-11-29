package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

public class HttpAioConnection extends AioConnection {

    public HttpAioConnection(AioConnector connector, SocketAddress address, Action action, Gateway gateway, AioClient client) throws IOException {
        super(connector, address, action, gateway, client);
    }

    @Override
    public boolean reset(Action action, Gateway gateway, AioClient client) {
        if (super.reset(action, gateway, client)) {
            request = new JestfulAioHttpClientRequest(action, connector, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
            response = new JestfulAioHttpClientResponse(action, connector, gateway);
            return true;
        }
        return false;
    }

}
