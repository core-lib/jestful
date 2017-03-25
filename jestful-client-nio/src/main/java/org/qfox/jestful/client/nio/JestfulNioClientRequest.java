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
    private final Object lock = new Object();
    private final String protocol = "HTTP/1.1";

    private NioByteArrayOutputStream out;
    private Writer writer;

    private boolean closed;

    private ByteBuffer head;
    private ByteBuffer body;

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

    public boolean read(SocketChannel channel) throws IOException {
        if (head == null || body == null) {
            NioByteArrayOutputStream baos = new NioByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos);

            String method = action.getRestful().getMethod();
            String uri = action.getURI();
            String query = action.getQuery();
            String command = method + " " + (uri == null || uri.length() == 0 ? "/" : uri) + (query == null || query.length() == 0 ? "" : "?" + query) + " " + protocol;
            setRequestHeader("", command);
            osw.write(command);
            osw.write(CTRL);

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
                    osw.write(CTRL);
                }
            }
            osw.write(CTRL);
            osw.flush();

            head = baos.toByteBuffer();
        }

        if (head.remaining() > 0) {
            channel.write(head);
        }
        if (head.remaining() == 0 && body.remaining() > 0) {
            channel.write(body);
        }

        return head.remaining() == 0 && body.remaining() == 0;
    }

    @Override
    public void close() throws IOException {
        if (closed) return;

        closed = true;

        IOUtils.close(writer);
        IOUtils.close(out);

        super.close();
    }

}
