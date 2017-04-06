package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulNioHttpsClientResponse extends JestfulNioHttpClientResponse {
    private final NioSSLChannel nioSSLChannel;
    private ByteBuffer cache = ByteBuffer.allocate(0);
    private ByteBuffer block = ByteBuffer.allocate(4096);

    protected JestfulNioHttpsClientResponse(Action action,
                                         Connector connector,
                                         Gateway gateway,
                                         NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway);
        this.nioSSLChannel = nioSSLChannel;
    }

    @Override
    public boolean load(ByteBuffer buffer) throws IOException {
        cache.compact();
        if (cache.remaining() >= buffer.remaining()) {
            cache.put(buffer);
        } else {
            ByteBuffer bigger = ByteBuffer.allocate(cache.remaining() + buffer.remaining());
            bigger.put(cache);
            bigger.put(buffer);
            cache = bigger;
        }
        cache.flip();
        nioSSLChannel.load(cache);

        block.clear();
        nioSSLChannel.read(block);
        block.flip();

        return super.load(block);
    }

    @Override
    public void close() throws IOException {
        super.close();
        IOKit.close(nioSSLChannel);
    }
}
