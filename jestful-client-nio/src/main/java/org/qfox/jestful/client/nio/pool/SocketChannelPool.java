package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.commons.pool.Pool;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public interface SocketChannelPool extends Pool<SocketAddress, SocketChannel> {

    @Override
    SocketChannel acquire(SocketAddress address);

    @Override
    void release(SocketAddress address, SocketChannel channel);

    @Override
    void destroy();

}
