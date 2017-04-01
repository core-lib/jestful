package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;

/**
 * Created by payne on 2017/4/2.
 */
public class NioConnection extends Connection {

    public NioConnection(NioRequest request, NioResponse response) {
        super(request, response);
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
