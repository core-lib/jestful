package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.pool.Validator;

public class NioConnectionValidator implements Validator<NioConnection> {
    @Override
    public boolean validate(NioConnection connection) {
        return connection.available();
    }
}
