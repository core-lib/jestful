package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.Producer;
import org.qfox.jestful.core.exception.JestfulRuntimeException;

import java.io.IOException;

public class AioConnectionProducer implements Producer<AioConnectionKey, AioConnection> {
    @Override
    public AioConnection produce(AioConnectionKey key) {
        try {
            return key.connector.aioConnect(key.action, key.gateway, key.client);
        } catch (IOException e) {
            throw new JestfulRuntimeException(e);
        }
    }
}
