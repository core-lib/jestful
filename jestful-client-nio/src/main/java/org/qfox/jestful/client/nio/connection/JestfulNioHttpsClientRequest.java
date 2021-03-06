package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulNioHttpsClientRequest extends JestfulNioHttpClientRequest {
    private final NioSSLChannel nioSSLChannel;

    protected JestfulNioHttpsClientRequest(Action action,
                                           Connector connector,
                                           Gateway gateway,
                                           int connTimeout,
                                           int readTimeout,
                                           int writeTimeout,
                                           NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.nioSSLChannel = nioSSLChannel;
    }

    @Override
    public int getIdleTimeout() {
        String keepAlive = getRequestHeader("Keep-Alive");
        if (keepAlive == null) return -1; // NOT SET
        // Keep-Alive: timeout=seconds
        StringTokenizer tokenizer = new StringTokenizer(keepAlive, ",; ");
        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();
            StringTokenizer t = new StringTokenizer(element, "=: ");
            if (t.countTokens() != 2) continue;
            String name = t.nextToken();
            String value = t.nextToken();
            if (name.equalsIgnoreCase("timeout")) return Integer.valueOf(value);
        }
        return -1; // NOT SET
    }

    @Override
    public void setIdleTimeout(int idleTimeout) {
        setRequestHeader("Keep-Alive", "timeout=" + idleTimeout);
    }

    @Override
    public void reset(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout) {
        super.reset(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        setRequestHeader("Connection", "keep-alive");
    }

    // 数据出站
    @Override
    public void copy(ByteBuffer buffer) throws IOException {
        if (head == null) {
            doWriteHeader();
        }

        nioSSLChannel.write(head);
        nioSSLChannel.write(body);

        nioSSLChannel.copy(buffer);
    }


    @Override
    public boolean move(int n) throws IOException {
        return nioSSLChannel.move(n) && head.remaining() == 0 && body.remaining() == 0;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
