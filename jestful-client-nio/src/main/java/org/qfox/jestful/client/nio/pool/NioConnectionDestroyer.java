package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.pool.Destroyer;

public class NioConnectionDestroyer implements Destroyer<NioConnection> {

    @Override
    public void destroy(NioConnection connection) {
        IOKit.close(connection);
    }
}
