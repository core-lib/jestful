package org.qfox.jestful.client;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

public class JestfulClientRequest implements Request {
    protected final Action action;
    protected final Connector connector;
    protected final Gateway gateway;
    protected final Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
    protected Request request;
    protected int connTimeout;
    protected int readTimeout;
    protected int writeTimeout;
    protected String characterEncoding;

    protected JestfulClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout) {
        super();
        this.action = action;
        this.connector = connector;
        this.gateway = gateway;
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
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
        header.put(name, new String[]{value});
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

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int timeout) {
        this.connTimeout = timeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public String getContentType() {
        return getRequestHeader("Content-Type");
    }

    public void setContentType(String type) {
        setRequestHeader("Content-Type", type);
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

    public InputStream getRequestInputStream() throws IOException {
        return getRequest().getRequestInputStream();
    }

    public OutputStream getRequestOutputStream() throws IOException {
        return getRequest().getRequestOutputStream();
    }

    @Override
    public Reader getRequestReader() throws IOException {
        return getRequest().getRequestReader();
    }

    @Override
    public Writer getRequestWriter() throws IOException {
        return getRequest().getRequestWriter();
    }

    public void connect() throws IOException {
        getRequest().connect();
    }

    public void close() throws IOException {
        if (request != null) {
            request.close();
        }
    }

    private synchronized Request getRequest() throws IOException {
        if (request != null) {
            return request;
        }
        Connection connection = connector.connect(action, gateway, null);
        request = connection.getRequest();
        for (Entry<String, String[]> entry : header.entrySet()) {
            request.setRequestHeaders(entry.getKey(), entry.getValue());
        }
        request.setConnTimeout(connTimeout);
        request.setReadTimeout(readTimeout);
        if (characterEncoding != null) {
            request.setCharacterEncoding(characterEncoding);
        }
        return request;
    }

}
