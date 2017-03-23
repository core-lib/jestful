package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Connector;
import org.qfox.jestful.client.JestfulClientResponse;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.*;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioClientResponse extends JestfulClientResponse {

    protected JestfulNioClientResponse(Action action, Connector connector, Gateway gateway) {
        super(action, connector, gateway);
    }

    @Override
    public String[] getHeaderKeys() {
        return new String[0];
    }

    @Override
    public String getResponseHeader(String name) {
        return null;
    }

    @Override
    public void setResponseHeader(String name, String value) {

    }

    @Override
    public String[] getResponseHeaders(String name) {
        return new String[0];
    }

    @Override
    public void setResponseHeaders(String name, String[] values) {

    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        return null;
    }

    @Override
    public OutputStream getResponseOutputStream() throws IOException {
        return null;
    }

    @Override
    public Reader getResponseReader() throws IOException {
        return null;
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        return null;
    }

    @Override
    public Status getResponseStatus() throws IOException {
        return null;
    }

    @Override
    public void setResponseStatus(Status status) throws IOException {

    }

    @Override
    public boolean isResponseSuccess() throws IOException {
        return false;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public void close() throws IOException {

    }
}
