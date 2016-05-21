package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestWrapper implements Request {
	private final Request request;

	public RequestWrapper(Request request) {
		super();
		this.request = request;
	}

	public String[] getHeaderKeys() {
		return request.getHeaderKeys();
	}

	public String getRequestHeader(String name) {
		return request.getRequestHeader(name);
	}

	public void setRequestHeader(String name, String value) {
		request.setRequestHeader(name, value);
	}

	public String[] getRequestHeaders(String name) {
		return request.getRequestHeaders(name);
	}

	public void setRequestHeaders(String name, String[] values) {
		request.setRequestHeaders(name, values);
	}

	public InputStream getRequestInputStream() throws IOException {
		return request.getRequestInputStream();
	}

	public OutputStream getRequestOutputStream() throws IOException {
		return request.getRequestOutputStream();
	}

	public int getConnectTimeout() {
		return request.getConnectTimeout();
	}

	public void setConnectTimeout(int timeout) {
		request.setConnectTimeout(timeout);
	}

	public int getTransferTimeout() {
		return request.getTransferTimeout();
	}

	public void setTransferTimeout(int timeout) {
		request.setTransferTimeout(timeout);
	}

	public void connect() throws IOException {
		request.connect();
	}
	
	public void close() throws IOException {
		request.close();
	}

}
