package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResponseWrapper implements Response {
	private final Response response;

	public ResponseWrapper(Response response) {
		super();
		this.response = response;
	}

	public String[] getHeaderKeys() {
		return response.getHeaderKeys();
	}

	public String getResponseHeader(String name) {
		return response.getResponseHeader(name);
	}

	public void setResponseHeader(String name, String value) {
		response.setResponseHeader(name, value);
	}

	public String[] getResponseHeaders(String name) {
		return response.getResponseHeaders(name);
	}

	public void setResponseHeaders(String name, String[] values) {
		response.setResponseHeaders(name, values);
	}

	public InputStream getResponseInputStream() throws IOException {
		return response.getResponseInputStream();
	}

	public OutputStream getResponseOutputStream() throws IOException {
		return response.getResponseOutputStream();
	}

	public Status getResponseStatus() throws IOException {
		return response.getResponseStatus();
	}

	public void setResponseStatus(Status status) throws IOException {
		response.setResponseStatus(status);
	}

	public boolean isResponseSuccess() throws IOException {
		return response.isResponseSuccess();
	}

}
