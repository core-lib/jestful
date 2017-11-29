package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.pool.Validator;

public class AioConnectionValidator implements Validator<AioConnection> {
    @Override
    public boolean validate(AioConnection connection) {
        return connection.available();
    }
}
