package org.qfox.jestful.server;

public interface RequestAttributes {

    /**
     * Standard Servlet 2.3+ spec request attributes for include URI and paths.
     * <p>
     * If included via a RequestDispatcher, the current resource will see the
     * originating request. Its own URI and paths are exposed as request
     * attributes.
     */
    String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
    String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
    String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
    String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
    String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

    /**
     * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
     * <p>
     * If forwarded to via a RequestDispatcher, the current resource will see
     * its own URI and paths. The originating URI and paths are exposed as
     * request attributes.
     */
    String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
    String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
    String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
    String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
    String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

    /**
     * Standard Servlet 2.3+ spec request attributes for error pages.
     * <p>
     * To be exposed to JSPs that are marked as error pages, when forwarding to
     * them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
    String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
    String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
    String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
    String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
    String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";

}
