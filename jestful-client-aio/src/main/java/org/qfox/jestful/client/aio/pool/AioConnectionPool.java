package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.Pool;

public interface AioConnectionPool extends Pool<AioConnectionKey, AioConnection> {
    @Override
    AioConnection acquire(AioConnectionKey key);

    @Override
    void release(AioConnectionKey key, AioConnection connection);

    @Override
    void destroy();
}
