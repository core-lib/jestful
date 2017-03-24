package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Connector;
import org.qfox.jestful.client.JestfulClientRequest;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.io.IOUtils;

import java.io.*;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioClientRequest extends JestfulClientRequest {
    private static final char[] CTRL = new char[]{'\r', '\n'};

    private final Object lock = new Object();
    private ByteArrayOutputStream out;
    private Writer writer;
    private ByteArrayInputStream in;
    private Reader reader;
    private boolean connected = false;

    protected JestfulNioClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout) {
        super(action, connector, gateway, connTimeout, readTimeout);
    }

    @Override
    public InputStream getRequestInputStream() throws IOException {
        if (in != null) {
            return in;
        }
        synchronized (lock) {
            if (in != null) {
                return in;
            }
            return in = new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        if (out != null) {
            return out;
        }
        synchronized (lock) {
            if (out != null) {
                return out;
            }
            return out = new ByteArrayOutputStream();
        }
    }

    @Override
    public Reader getRequestReader() throws IOException {
        if (reader != null) {
            return reader;
        }
        synchronized (lock) {
            if (reader != null) {
                return reader;
            }
            return reader = characterEncoding != null ? new InputStreamReader(getRequestInputStream(), characterEncoding) : new InputStreamReader(getRequestInputStream());
        }
    }

    @Override
    public Writer getRequestWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        synchronized (lock) {
            if (writer != null) {
                return writer;
            }
            return writer = characterEncoding != null ? new OutputStreamWriter(getRequestOutputStream(), characterEncoding) : new OutputStreamWriter(getRequestOutputStream());
        }
    }

    @Override
    public void connect() throws IOException {
        if (connected) {
            return;
        }
        connected = true;

        String method = action.getRestful().getMethod();
        String uri = action.getURI();
        String query = action.getQuery();

        Writer writer = getRequestWriter();
        writer.write(method + " " + uri + (query == null || query.isEmpty() ? "" : "?" + query));
        writer.write(CTRL);
        for (Map.Entry<String, String[]> entry : header.entrySet()) {
            for (String value : entry.getValue()) {

            }
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(writer);
        IOUtils.close(out);
    }
}
