package org.qfox.jestful.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

public class JestfulClientResponse implements Response {
	private final Action action;
	private final Connector connector;
	private final Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
	private Response response;

	JestfulClientResponse(Action action, Connector connector) {
		super();
		this.action = action;
		this.connector = connector;
	}

	public String[] getHeaderKeys() {
		if (response != null) {
			return response.getHeaderKeys();
		}
		return header.keySet().toArray(new String[0]);
	}

	public String getResponseHeader(String name) {
		if (response != null) {
			return response.getResponseHeader(name);
		}
		return header.containsKey(name) ? header.get(name)[0] : null;
	}

	public void setResponseHeader(String name, String value) {
		if (response != null) {
			response.setResponseHeader(name, value);
			return;
		}
		header.put(name, new String[] { value });
	}

	public String[] getResponseHeaders(String name) {
		if (response != null) {
			return response.getResponseHeaders(name);
		}
		return header.get(name).clone();
	}

	public void setResponseHeaders(String name, String[] values) {
		if (response != null) {
			response.setResponseHeaders(name, values);
		}
		header.put(name, values.clone());
	}

	public InputStream getResponseInputStream() throws IOException {
		return getResponse().getResponseInputStream();
	}

	public OutputStream getResponseOutputStream() throws IOException {
		return getResponse().getResponseOutputStream();
	}

	public Status getResponseStatus() throws IOException {
		return getResponse().getResponseStatus();
	}

	public void setResponseStatus(Status status) throws IOException {
		getResponse().setResponseStatus(status);
	}

	public boolean isResponseSuccess() throws IOException {
		return getResponse().isResponseSuccess();
	}

	private synchronized Response getResponse() throws IOException {
		if (response != null) {
			return response;
		}
		Connection connection = connector.connect(action);
		response = connection.getResponse();
		for (Entry<String, String[]> entry : header.entrySet()) {
			response.setResponseHeaders(entry.getKey(), entry.getValue());
		}
		return response;
	}

}
