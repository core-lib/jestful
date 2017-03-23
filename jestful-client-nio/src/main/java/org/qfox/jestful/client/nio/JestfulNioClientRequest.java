package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Connector;
import org.qfox.jestful.client.JestfulClientRequest;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

import java.io.*;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioClientRequest extends JestfulClientRequest {

    protected JestfulNioClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout) {
        super(action, connector, gateway, connTimeout, readTimeout);
    }

    @Override
    public String[] getHeaderKeys() {
        return new String[0];
    }

    @Override
    public String getRequestHeader(String name) {
        return null;
    }

    @Override
    public void setRequestHeader(String name, String value) {

    }

    @Override
    public String[] getRequestHeaders(String name) {
        return new String[0];
    }

    @Override
    public void setRequestHeaders(String name, String[] values) {

    }

    @Override
    public int getConnTimeout() {
        return 0;
    }

    @Override
    public void setConnTimeout(int timeout) {

    }

    @Override
    public int getReadTimeout() {
        return 0;
    }

    @Override
    public void setReadTimeout(int timeout) {

    }

    @Override
    public void connect() throws IOException {

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
    public InputStream getRequestInputStream() throws IOException {
        return null;
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        return null;
    }

    @Override
    public Reader getRequestReader() throws IOException {
        return null;
    }

    @Override
    public Writer getRequestWriter() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
