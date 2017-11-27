package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.Pool;

import java.net.SocketAddress;

public interface NioConnectionPool extends Pool<SocketAddress, NioConnection> {
    @Override
    NioConnection acquire(SocketAddress address);

    @Override
    void release(SocketAddress address, NioConnection connection);

    @Override
    void destroy();
}
