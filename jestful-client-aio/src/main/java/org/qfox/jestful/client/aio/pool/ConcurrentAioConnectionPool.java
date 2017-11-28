package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.ConcurrentPool;
import org.qfox.jestful.commons.pool.Destroyer;

import java.net.SocketAddress;

public class ConcurrentAioConnectionPool extends ConcurrentPool<SocketAddress, AioConnection> implements AioConnectionPool {

    public ConcurrentAioConnectionPool() {
        this(new AioConnectionDestroyer());
    }

    public ConcurrentAioConnectionPool(Destroyer<AioConnection> destroyer) {
        super(destroyer);
    }
}
