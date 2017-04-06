package org.qfox.jestful.client.aio.connection;

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
public class JestfulAioHttpsClientResponse extends JestfulAioHttpClientResponse {
    private final AioSSLChannel aioSSLChannel;
    private ByteBuffer cache = ByteBuffer.allocate(0);
    private ByteBuffer block = ByteBuffer.allocate(4096);

    protected JestfulAioHttpsClientResponse(Action action,
                                            Connector connector,
                                            Gateway gateway,
                                            AioSSLChannel aioSSLChannel) {
        super(action, connector, gateway);
        this.aioSSLChannel = aioSSLChannel;
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
        aioSSLChannel.load(cache);

        block.clear();
        aioSSLChannel.read(block);
        block.flip();

        return super.load(block);
    }

    @Override
    public void close() throws IOException {
        super.close();
        IOKit.close(aioSSLChannel);
    }
}
