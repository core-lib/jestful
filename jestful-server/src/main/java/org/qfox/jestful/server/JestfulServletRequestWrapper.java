package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JestfulServletRequestWrapper extends JestfulServletRequest {
    private final JestfulServletRequest request;

    public JestfulServletRequestWrapper(JestfulServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String[] getHeaderKeys() {
        return request.getHeaderKeys();
    }

    @Override
    public String getRequestHeader(String name) {
        return request.getRequestHeader(name);
    }

    @Override
    public void setRequestHeader(String name, String value) {
        request.setRequestHeader(name, value);
    }

    @Override
    public String[] getRequestHeaders(String name) {
        return request.getRequestHeaders(name);
    }

    @Override
    public void setRequestHeaders(String name, String[] values) {
        request.setRequestHeaders(name, values);
    }

    @Override
    public InputStream getRequestInputStream() throws IOException {
        return request.getRequestInputStream();
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        return request.getRequestOutputStream();
    }

}
