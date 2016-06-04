package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

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

	public int getConnTimeout() {
		return request.getConnTimeout();
	}

	public void setConnTimeout(int timeout) {
		request.setConnTimeout(timeout);
	}

	public int getReadTimeout() {
		return request.getReadTimeout();
	}

	public void setReadTimeout(int timeout) {
		request.setReadTimeout(timeout);
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		request.setCharacterEncoding(env);
	}

	public void connect() throws IOException {
		request.connect();
	}

	public void close() throws IOException {
		request.close();
	}

}
