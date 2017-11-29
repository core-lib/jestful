package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.ConcurrentPool;
import org.qfox.jestful.commons.pool.Destroyer;
import org.qfox.jestful.commons.pool.Producer;
import org.qfox.jestful.commons.pool.Validator;

public class ConcurrentNioConnectionPool extends ConcurrentPool<NioConnectionKey, NioConnection> implements NioConnectionPool {

    public ConcurrentNioConnectionPool() {
        this(new NioConnectionProducer(), new NioConnectionValidator(), new NioConnectionDestroyer());
    }

    public ConcurrentNioConnectionPool(Producer<NioConnectionKey, NioConnection> producer, Validator<NioConnection> validator, Destroyer<NioConnection> destroyer) {
        super(producer, validator, destroyer);
    }
}
