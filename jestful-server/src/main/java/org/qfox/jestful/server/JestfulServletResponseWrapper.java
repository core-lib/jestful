package org.qfox.jestful.server;

import org.qfox.jestful.core.Status;

import java.io.*;

public class JestfulServletResponseWrapper extends JestfulServletResponse {
    private final JestfulServletResponse response;

    public JestfulServletResponseWrapper(JestfulServletResponse response) {
        super(response);
        this.response = response;
    }

    @Override
    public Status getResponseStatus() throws IOException {
        return response.getResponseStatus();
    }

    @Override
    public void setResponseStatus(Status status) throws IOException {
        response.setResponseStatus(status);
    }

    @Override
    public boolean isResponseSuccess() throws IOException {
        return response.isResponseSuccess();
    }

    @Override
    public String[] getHeaderKeys() {
        return response.getHeaderKeys();
    }

    @Override
    public String getResponseHeader(String name) {
        return response.getResponseHeader(name);
    }

    @Override
    public void setResponseHeader(String name, String value) {
        response.setResponseHeader(name, value);
    }

    @Override
    public String[] getResponseHeaders(String name) {
        return response.getResponseHeaders(name);
    }

    @Override
    public void setResponseHeaders(String name, String[] values) {
        response.setResponseHeaders(name, values);
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        return response.getResponseInputStream();
    }

    @Override
    public OutputStream getResponseOutputStream() throws IOException {
        return response.getResponseOutputStream();
    }

    @Override
    public Reader getResponseReader() throws IOException {
        return response.getResponseReader();
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        return response.getResponseWriter();
    }

}
