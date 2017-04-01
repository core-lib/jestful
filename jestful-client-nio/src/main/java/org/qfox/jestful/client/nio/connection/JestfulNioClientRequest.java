package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.JestfulClientRequest;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioByteArrayOutputStream;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class JestfulNioClientRequest extends JestfulClientRequest implements NioRequest {
    private final Object lock = new Object();
    private final String protocol = "HTTP/1.1";

    private NioByteArrayOutputStream out;
    private Writer writer;

    private boolean closed;

    private ByteBuffer head;
    private ByteBuffer body;

    protected JestfulNioClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
    }

    @Override
    public InputStream getRequestInputStream() throws IOException {
        throw new UnsupportedOperationException();
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
            return out = new NioByteArrayOutputStream();
        }
    }

    @Override
    public Reader getRequestReader() throws IOException {
        throw new UnsupportedOperationException();
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

    }

    private void doWriteHeader() throws IOException {
        NioByteArrayOutputStream baos = new NioByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos);

        String method = action.getRestful().getMethod();
        String uri = new URL(action.getURL()).getFile();
        String command = method + " " + uri + " " + protocol;
        setRequestHeader("", command);
        osw.write(command);
        osw.write(CRLF);

        String host = action.getHost();
        Integer port = action.getPort();
        setRequestHeader("Host", host + (port == null || port < 0 ? "" : ":" + port));

        body = out != null ? out.toByteBuffer() : ByteBuffer.wrap(new byte[0]);
        setRequestHeader("Content-Length", String.valueOf(body.remaining()));

        for (Map.Entry<String, String[]> entry : header.entrySet()) {
            String name = entry.getKey();
            if (name == null || name.length() == 0) continue;

            for (String value : entry.getValue()) {
                if (value == null) continue;

                osw.write(name);
                osw.write(SPRT);
                osw.write(value);
                osw.write(CRLF);
            }
        }
        osw.write(CRLF);
        osw.flush();

        head = baos.toByteBuffer();
    }

    public boolean send(ByteBuffer buffer) throws IOException {
        if (head == null) {
            doWriteHeader();
        }

        if (head.remaining() == 0 && body.remaining() == 0) {
            return true;
        }

        while (buffer.hasRemaining() && head.hasRemaining()) {
            buffer.put(head.get());
        }
        while (buffer.hasRemaining() && body.hasRemaining()) {
            buffer.put(body.get());
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        if (closed) return;

        closed = true;

        IOKit.close(writer);
        IOKit.close(out);

        super.close();
    }

}
