package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.ConcurrentPool;
import org.qfox.jestful.commons.pool.Destroyer;
import org.qfox.jestful.commons.pool.Producer;
import org.qfox.jestful.commons.pool.Validator;

public class ConcurrentAioConnectionPool extends ConcurrentPool<AioConnectionKey, AioConnection> implements AioConnectionPool {

    public ConcurrentAioConnectionPool() {
        this(new AioConnectionProducer(), new AioConnectionValidator(), new AioConnectionDestroyer());
    }

    public ConcurrentAioConnectionPool(Producer<AioConnectionKey, AioConnection> producer, Validator<AioConnection> validator, Destroyer<AioConnection> destroyer) {
        super(producer, validator, destroyer);
    }
}
