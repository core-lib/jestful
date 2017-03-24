package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Connector;
import org.qfox.jestful.client.JestfulClientRequest;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class JestfulNioClientRequest extends JestfulClientRequest {
    private static final char[] CTRL = new char[]{'\r', '\n'};
    private static final char[] SPRT = new char[]{':', ' '};

    private final Object lock = new Object();

    private ByteArrayOutputStream out;
    private Writer writer;

    private boolean closed;

    private byte[] bytes;
    private int position;

    protected JestfulNioClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout) {
        super(action, connector, gateway, connTimeout, readTimeout);
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
            return out = new CloseableByteArrayOutputStream();
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

    public boolean writeTo(SocketChannel channel, ByteBuffer buffer) throws IOException {
        if (bytes == null) {
            close();
        }

        if (position < bytes.length) {
            int length = Math.min(buffer.capacity(), bytes.length - position);
            buffer.clear();
            buffer.put(bytes, position, length);
            buffer.flip();
            length = channel.write(buffer);
            position += length;
            return position >= bytes.length;
        } else {
            return true;
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) return;

        IOUtils.close(writer);
        IOUtils.close(out);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos);

        String method = action.getRestful().getMethod();
        String uri = action.getURI();
        String query = action.getQuery();
        osw.write(method + " " + uri + (query == null || query.length() == 0 ? "" : "?" + query));
        osw.write(CTRL);

        String host = action.getHost();
        Integer port = action.getPort();
        osw.write("Host");
        osw.write(SPRT);
        osw.write(host + (port == null || port < 0 ? "" : ":" + port));
        osw.write(CTRL);

        byte[] body = out == null ? new byte[0] : out.toByteArray();
        osw.write("Content-Length");
        osw.write(SPRT);
        osw.write(String.valueOf(body.length));
        osw.write(CTRL);

        for (Map.Entry<String, String[]> entry : header.entrySet()) {
            String name = entry.getKey();
            if (name == null) continue;
            for (String value : entry.getValue()) {
                if (value == null) continue;
                osw.write(name);
                osw.write(SPRT);
                osw.write(value);
                osw.write(CTRL);
            }
        }
        osw.write(CTRL);
        osw.flush();

        byte[] head = baos.toByteArray();

        bytes = new byte[head.length + body.length];
        System.arraycopy(head, 0, bytes, 0, head.length);
        System.arraycopy(body, 0, bytes, head.length, body.length);
        position = 0;

        super.close();
    }

}
