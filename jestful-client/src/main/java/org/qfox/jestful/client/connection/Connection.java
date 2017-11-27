package org.qfox.jestful.client.connection;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

import java.net.SocketAddress;

public class Connection {
    protected final SocketAddress address;
    protected final Request request;
    protected final Response response;

    public Connection(SocketAddress address, Request request, Response response) {
        super();
        this.address = address;
        this.request = request;
        this.response = response;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

}
