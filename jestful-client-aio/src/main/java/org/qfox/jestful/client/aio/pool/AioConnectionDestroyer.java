package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.pool.Destroyer;

public class AioConnectionDestroyer implements Destroyer<AioConnection> {

    @Override
    public void destroy(AioConnection connection) {
        IOKit.close(connection);
    }
}
