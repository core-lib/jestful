package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.commons.pool.Pool;

import java.net.SocketAddress;

public interface SocketChannelConnectionPool extends Pool<SocketAddress, SocketChannelConnection> {

    @Override
    SocketChannelConnection acquire(SocketAddress addr);

    @Override
    void release(SocketAddress addr, SocketChannelConnection scc);

    @Override
    void destroy();

}
