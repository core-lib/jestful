package org.qfox.jestful.server;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class ServletInformation implements Serializable {
	private static final long serialVersionUID = -7017889944140453825L;

	private final String protocol;
	private final String method;
	private final String requestURI;
	private final String query;
	private final ServletDispatcher dispatcher;

	public ServletInformation(HttpServletRequest request) {
		this.protocol = request.getProtocol();
		this.method = request.getMethod();
		if (request.getAttribute(ServletConstants.INCLUDE_REQUEST_URI_ATTRIBUTE) != null) {
			this.dispatcher = ServletDispatcher.INCLUDE;
			this.requestURI = (String) request.getAttribute(ServletConstants.INCLUDE_REQUEST_URI_ATTRIBUTE);
			this.query = (String) request.getAttribute(ServletConstants.INCLUDE_QUERY_STRING_ATTRIBUTE);
		} else if (request.getAttribute(ServletConstants.FORWARD_REQUEST_URI_ATTRIBUTE) != null) {
			this.dispatcher = ServletDispatcher.FORWARD;
			this.requestURI = request.getRequestURI();
			this.query = request.getQueryString();
		} else {
			this.dispatcher = ServletDispatcher.REQUEST;
			this.requestURI = request.getRequestURI();
			this.query = request.getQueryString();
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

	public String getQuery() {
		return query;
	}

	public ServletDispatcher getDispatcher() {
		return dispatcher;
	}

}
