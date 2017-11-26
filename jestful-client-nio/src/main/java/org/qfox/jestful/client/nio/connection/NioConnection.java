package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;

import java.net.SocketAddress;

/**
 * Created by payne on 2017/4/2.
 */
public class NioConnection extends Connection {

    public NioConnection(SocketAddress address, NioRequest request, NioResponse response) {
        super(address, request, response);
    }

    @Override
    public NioRequest getRequest() {
        return (NioRequest) super.getRequest();
    }

    @Override
    public NioResponse getResponse() {
        return (NioResponse) super.getResponse();
    }
}
