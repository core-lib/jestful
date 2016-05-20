package org.qfox.jestful.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

public class JestfulClientRequest implements Request {
	private final Action action;
	private final Connector connector;
	private final Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
	private Request request;
	private int connectTimeout;
	private int transferTimeout;

	JestfulClientRequest(Action action, Connector connector) {
		super();
		this.action = action;
		this.connector = connector;
	}

	public String[] getHeaderKeys() {
		if (request != null) {
			return request.getHeaderKeys();
		}
		return header.keySet().toArray(new String[0]);
	}

	public String getRequestHeader(String name) {
		if (request != null) {
			return request.getRequestHeader(name);
		}
		return header.containsKey(name) ? header.get(name)[0] : null;
	}

	public void setRequestHeader(String name, String value) {
		if (request != null) {
			request.setRequestHeader(name, value);
			return;
		}
		header.put(name, new String[] { value });
	}

	public String[] getRequestHeaders(String name) {
		if (request != null) {
			return request.getRequestHeaders(name);
		}
		return header.get(name).clone();
	}

	public void setRequestHeaders(String name, String[] values) {
		if (request != null) {
			request.setRequestHeaders(name, values);
		}
		header.put(name, values.clone());
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getTransferTimeout() {
		return transferTimeout;
	}

	public void setTransferTimeout(int transferTimeout) {
		this.transferTimeout = transferTimeout;
	}

	public InputStream getRequestInputStream() throws IOException {
		return getRequest().getRequestInputStream();
	}

	public OutputStream getRequestOutputStream() throws IOException {
		return getRequest().getRequestOutputStream();
	}

	public void connect() throws IOException {
		getRequest().connect();
	}

	private synchronized Request getRequest() throws IOException {
		if (request != null) {
			return request;
		}
		Connection connection = connector.connect(action);
		request = connection.getRequest();
		for (Entry<String, String[]> entry : header.entrySet()) {
			request.setRequestHeaders(entry.getKey(), entry.getValue());
		}
		request.setConnectTimeout(connectTimeout);
		request.setTransferTimeout(transferTimeout);
		return request;
	}

}
