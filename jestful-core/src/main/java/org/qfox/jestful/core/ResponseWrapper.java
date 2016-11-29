package org.qfox.jestful.core;

import java.io.*;

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

    @Override
    public Reader getResponseReader() throws IOException {
        return response.getResponseReader();
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        return response.getResponseWriter();
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

    public String getContentType() {
        return response.getContentType();
    }

    public void setContentType(String type) {
        response.setContentType(type);
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        response.setCharacterEncoding(env);
    }

    public void close() throws IOException {
        response.close();
    }

}
