package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 */
public class JestfulNioHttpsClientRequest extends JestfulNioHttpClientRequest {
    private final NioSSLChannel nioSSLChannel;

    public JestfulNioHttpsClientRequest(Action action,
                                        Connector connector,
                                        Gateway gateway,
                                        int connTimeout,
                                        int readTimeout,
                                        int writeTimeout,
                                        NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.nioSSLChannel = nioSSLChannel;
    }

    // 数据出站
    @Override
    public boolean copy(ByteBuffer buffer) throws IOException {
        if (head == null) {
            doWriteHeader();
        }

        if (head.remaining() == 0 && body.remaining() == 0) return nioSSLChannel.wrap(EMPTY, buffer);

        if (head.hasRemaining() && buffer.hasRemaining()) nioSSLChannel.wrap(head, buffer);
        if (body.hasRemaining() && buffer.hasRemaining()) nioSSLChannel.wrap(body, buffer);

        return false;
    }

}
