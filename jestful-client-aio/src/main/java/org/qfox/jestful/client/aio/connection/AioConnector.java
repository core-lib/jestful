package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public interface AioConnector extends Connector {

    AioConnection aioConnect(Action action, Gateway gateway, AioClient client) throws IOException;

}
