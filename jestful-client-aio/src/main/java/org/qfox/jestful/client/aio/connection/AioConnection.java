package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioRequest;
import org.qfox.jestful.client.aio.AioResponse;
import org.qfox.jestful.client.connection.Connection;

import java.net.SocketAddress;

/**
 * Created by payne on 2017/4/2.
 */
public class AioConnection extends Connection {

    public AioConnection(SocketAddress address, AioRequest request, AioResponse response) {
        super(address, request, response);
    }

    @Override
    public AioRequest getRequest() {
        return (AioRequest) super.getRequest();
    }

    @Override
    public AioResponse getResponse() {
        return (AioResponse) super.getResponse();
    }
}
