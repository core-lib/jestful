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
    public boolean receive(ByteBuffer buffer) throws IOException {
        cache.compact();
        if (cache.remaining() > buffer.remaining()) {
            cache.put(buffer);
        } else {
            ByteBuffer bigger = ByteBuffer.allocate(cache.position() + buffer.remaining());
            bigger.put(cache);
            bigger.put(buffer);
            cache = bigger;
        }

        cache.flip();
        ByteBuffer buf = ByteBuffer.allocate(4096);
        nioSSLChannel.unwrap(cache, buf);

        return super.receive(buf);
    }
}
