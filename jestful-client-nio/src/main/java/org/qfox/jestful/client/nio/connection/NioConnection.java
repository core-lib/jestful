package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;

/**
 * Created by payne on 2017/4/2.
 */
public class NioConnection extends Connection {

    public NioConnection(SocketAddress address, NioRequest request, NioResponse response) {
        super(address, request, response);
    }

    public boolean isKeepAlive() {
        return getRequest().isKeepAlive() && getResponse().isKeepAlive();
    }

    public void setKeepAlive(boolean keepAlive) {
        getRequest().setKeepAlive(keepAlive);
    }

    public void clear() {
        getRequest().clear();
        getResponse().clear();
    }

    public void reset(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout) {
        getRequest().reset(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        getResponse().reset(action, connector, gateway);
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
