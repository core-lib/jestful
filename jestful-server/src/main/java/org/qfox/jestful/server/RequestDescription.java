package org.qfox.jestful.server;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class RequestDescription implements RequestAttributes, Serializable {
    private static final long serialVersionUID = -7017889944140453825L;

    private final String protocol;
    private final String method;
    private final String requestURI;
    private final String servletURI;
    private final String query;
    private final DispatcherType dispatcherType;

    public RequestDescription(HttpServletRequest request) {
        this.protocol = request.getProtocol();
        this.method = request.getMethod();
        this.dispatcherType = request.getDispatcherType();
        switch (dispatcherType) {
            case INCLUDE:
                this.requestURI = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
                this.servletURI = (String) request.getAttribute(INCLUDE_SERVLET_PATH_ATTRIBUTE);
                this.query = (String) request.getAttribute(INCLUDE_QUERY_STRING_ATTRIBUTE);
                break;
            default:
                this.requestURI = request.getRequestURI();
                this.servletURI = request.getServletPath();
                this.query = request.getQueryString();
                break;
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getServletURI() {
        return servletURI;
    }

    public String getQuery() {
        return query;
    }

    public DispatcherType getDispatcherType() {
        return dispatcherType;
    }
}
