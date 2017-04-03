package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 */
public class JestfulNioHttpsClientResponse extends JestfulNioHttpClientResponse {
    protected final NioSSLChannel nioSSLChannel;
    protected ByteBuffer cache = ByteBuffer.allocate(0);

    public JestfulNioHttpsClientResponse(Action action,
                                         Connector connector,
                                         Gateway gateway,
                                         NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway);
        this.nioSSLChannel = nioSSLChannel;
    }

    @Override
    public boolean load(ByteBuffer buffer) throws IOException {
        return false;
    }
}
