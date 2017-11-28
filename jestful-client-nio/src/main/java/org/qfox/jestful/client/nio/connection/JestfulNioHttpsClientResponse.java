package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulNioHttpsClientResponse extends JestfulNioHttpClientResponse {
    private final NioSSLChannel nioSSLChannel;
    private final ByteBuffer block = ByteBuffer.allocate(4096);

    protected JestfulNioHttpsClientResponse(Action action,
                                            Connector connector,
                                            Gateway gateway,
                                            NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway);
        this.nioSSLChannel = nioSSLChannel;
    }

    @Override
    public void clear() {
        super.clear();
        if (this.block != null) this.block.clear();
    }

    @Override
    public boolean load(ByteBuffer buffer) throws IOException {
        nioSSLChannel.load(buffer);

        block.clear();
        nioSSLChannel.read(block);
        block.flip();

        return super.load(block);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
