package org.qfox.jestful.client;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;

public interface ReusableResponse extends Response {

    boolean isKeepAlive();

    /**
     * return the response specified Keep-Alive timeout seconds.
     * if NOT SET return -1.
     * and any long value less than zero should be recognized as NOT SET.
     *
     * @return the response specified Keep-Alive timeout seconds. or -1 if NOT SET
     */
    long getKeepAlive();

    void clear();

    void reset(Action action, Connector connector, Gateway gateway);

}
