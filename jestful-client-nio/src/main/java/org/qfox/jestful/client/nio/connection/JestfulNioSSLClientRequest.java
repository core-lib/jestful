package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import javax.net.ssl.SSLEngine;

/**
 * Created by payne on 2017/4/1.
 */
public class JestfulNioSSLClientRequest extends JestfulNioClientRequest {
    protected final SSLEngine engine;

    public JestfulNioSSLClientRequest(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout, SSLEngine engine) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.engine = engine;
    }

}
