package org.qfox.jestful.client;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

public interface ReusableRequest extends Request {

    boolean isKeepAlive();

    void setKeepAlive(boolean keepAlive);

    void clear();

    void reset(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout);

}
