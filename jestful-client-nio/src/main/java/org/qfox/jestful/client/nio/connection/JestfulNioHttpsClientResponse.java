package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import javax.net.ssl.SSLEngine;

/**
 * Created by payne on 2017/4/1.
 */
public class JestfulNioHttpsClientResponse extends JestfulNioHttpClientResponse {
    protected final SSLEngine engine;

    public JestfulNioHttpsClientResponse(Action action, Connector connector, Gateway gateway, SSLEngine engine) {
        super(action, connector, gateway);
        this.engine = engine;
    }
}
