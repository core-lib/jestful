package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.ConcurrentPool;
import org.qfox.jestful.commons.pool.Destroyer;

import java.net.SocketAddress;

public class ConcurrentNioConnectionPool extends ConcurrentPool<SocketAddress, NioConnection> implements NioConnectionPool {

    public ConcurrentNioConnectionPool() {
        this(new NioConnectionDestroyer());
    }

    public ConcurrentNioConnectionPool(Destroyer<NioConnection> destroyer) {
        super(destroyer);
    }
}
