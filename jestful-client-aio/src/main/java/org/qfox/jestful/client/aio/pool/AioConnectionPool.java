package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.Pool;

import java.net.SocketAddress;

public interface AioConnectionPool extends Pool<SocketAddress, AioConnection> {
    @Override
    AioConnection acquire(SocketAddress address);

    @Override
    void release(SocketAddress address, AioConnection connection);

    @Override
    void destroy();
}
