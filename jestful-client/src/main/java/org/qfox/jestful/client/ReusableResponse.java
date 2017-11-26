package org.qfox.jestful.client;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;

public interface ReusableResponse extends Response {

    boolean isKeepAlive();

    void clear();

    void reset(Action action, Connector connector, Gateway gateway);

}
