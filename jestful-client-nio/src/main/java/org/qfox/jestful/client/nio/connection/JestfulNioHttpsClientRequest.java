package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import javax.net.ssl.SSLEngine;

/**
 * Created by payne on 2017/4/1.
 */
public class JestfulNioHttpsClientRequest extends JestfulNioHttpClientRequest {
    protected final SSLEngine engine;

    public JestfulNioHttpsClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout, SSLEngine engine) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.engine = engine;
    }

}
