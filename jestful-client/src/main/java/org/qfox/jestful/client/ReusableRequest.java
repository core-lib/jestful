package org.qfox.jestful.client;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

public interface ReusableRequest extends Request {

    boolean isKeepAlive();

    void setKeepAlive(boolean keepAlive);

    /**
     * return the request specified Keep-Alive timeout seconds.
     * if NOT SET return -1.
     * and any long value that less than zero should be recognized as NOT SET.
     *
     * @return the request specified Keep-Alive timeout seconds. or -1 if NOT SET
     */
    int getIdleTimeout();

    /**
     * set the request Keep-Alive timeout seconds.
     * and any long value that less than zero should be recognized as NOT SET.
     */
    void setIdleTimeout(int idleTimeout);

    void clear();

    void reset(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout);

}
