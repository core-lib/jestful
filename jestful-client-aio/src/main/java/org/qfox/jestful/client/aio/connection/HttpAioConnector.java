package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.aio.AioRequest;
import org.qfox.jestful.client.aio.AioResponse;
import org.qfox.jestful.client.connection.HttpConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpAioConnector extends HttpConnector implements AioConnector {

    @Override
    public AioConnection aioConnect(Action action, Gateway gateway, AioClient client) throws IOException {
        SocketAddress address = aioAddress(action, gateway, client);
        AioRequest request = new JestfulAioHttpClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
        AioResponse response = new JestfulAioHttpClientResponse(action, this, gateway);
        return new AioConnection(address, request, response);
    }

    @Override
    public SocketAddress aioAddress(Action action, Gateway gateway, AioClient client) throws IOException {
        return address(action, gateway, client);
    }
}
