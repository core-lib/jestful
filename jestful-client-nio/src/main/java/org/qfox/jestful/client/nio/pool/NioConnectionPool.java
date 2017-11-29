package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.Pool;

public interface NioConnectionPool extends Pool<NioConnectionKey, NioConnection> {
    @Override
    NioConnection acquire(NioConnectionKey key);

    @Override
    void release(NioConnectionKey key, NioConnection connection);

    @Override
    void destroy();
}
