package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public interface NioConnector extends Connector {

    NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException;

}
