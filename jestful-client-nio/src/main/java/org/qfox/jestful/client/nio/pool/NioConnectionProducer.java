package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.Producer;
import org.qfox.jestful.core.exception.JestfulRuntimeException;

import java.io.IOException;

public class NioConnectionProducer implements Producer<NioConnectionKey, NioConnection> {
    @Override
    public NioConnection produce(NioConnectionKey key) {
        try {
            return key.connector.nioConnect(key.action, key.gateway, key.client);
        } catch (IOException e) {
            throw new JestfulRuntimeException(e);
        }
    }
}
