package org.qfox.jestful.client;

import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

public class JestfulClientResponse implements Response, ReusableResponse {
    protected final Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
    protected Action action;
    protected Connector connector;
    protected Gateway gateway;
    protected Response response;
    protected String characterEncoding;

    protected JestfulClientResponse(Action action, Connector connector, Gateway gateway) {
        reset(action, connector, gateway);
    }

    @Override
    public boolean isKeepAlive() {
        return false;
    }

    @Override
    public int getIdleTimeout() {
        return -1;
    }

    @Override
    public void clear() {
        this.header.clear();
        this.action = null;
        this.connector = null;
        this.gateway = null;
        this.response = null;
        this.characterEncoding = null;
    }

    @Override
    public void reset(Action action, Connector connector, Gateway gateway) {
        this.clear();
        this.action = action;
        this.connector = connector;
        this.gateway = gateway;
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
        header.put(name, new String[]{value});
    }

    public String[] getResponseHeaders(String name) {
        if (response != null) {
            return response.getResponseHeaders(name);
        }
        String[] values = header.get(name);
        return values != null ? values.clone() : null;
    }

    public void setResponseHeaders(String name, String[] values) {
        if (response != null) {
            response.setResponseHeaders(name, values);
        }
        String[] clones = values != null ? values.clone() : null;
        header.put(name, clones);
    }

    public InputStream getResponseInputStream() throws IOException {
        return getResponse().getResponseInputStream();
    }

    public OutputStream getResponseOutputStream() throws IOException {
        return getResponse().getResponseOutputStream();
    }

    @Override
    public Reader getResponseReader() throws IOException {
        return getResponse().getResponseReader();
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        return getResponse().getResponseWriter();
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

    public String getContentType() {
        return getResponseHeader("Content-Type");
    }

    public void setContentType(String type) {
        setResponseHeader("Content-Type", type);
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        if (Charset.isSupported(env)) {
            characterEncoding = env;
        } else {
            throw new UnsupportedEncodingException(env);
        }
    }

    @Override
    public boolean isCommitted() {
        return response != null && response.isCommitted();
    }

    @Override
    public void reset() {
        if (response != null) response.reset();
    }

    public void close() throws IOException {
        if (response != null) response.close();
    }

    private synchronized Response getResponse() throws IOException {
        if (response != null) {
            return response;
        }
        Connection connection = connector.connect(action, gateway, null);
        response = connection.getResponse();
        for (Entry<String, String[]> entry : header.entrySet()) {
            response.setResponseHeaders(entry.getKey(), entry.getValue());
        }
        if (characterEncoding != null) {
            response.setCharacterEncoding(characterEncoding);
        }
        return response;
    }

}
