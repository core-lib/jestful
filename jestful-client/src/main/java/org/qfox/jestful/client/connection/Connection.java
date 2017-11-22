package org.qfox.jestful.client.connection;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

public class Connection {
    private final Request request;
    private final Response response;

    public Connection(Request request, Response response) {
        super();
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

}
