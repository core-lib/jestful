package org.qfox.jestful.client;

import org.qfox.jestful.core.Response;

import java.io.Serializable;

public final class Header implements Serializable {
    private static final long serialVersionUID = -2109010455698947217L;
    private final Response response;

    public Header(Response response) {
        this.response = response;
    }

    public String[] getNames() {
        return response.getHeaderKeys();
    }

    public String getHeader(String name) {
        return response.getResponseHeader(name);
    }

    public String[] getHeaders(String name) {
        return response.getResponseHeaders(name);
    }

}
