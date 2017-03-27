package org.qfox.jestful.client.nio.timeout;

import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public interface TimeoutManager {

    void addConnTimeoutHandler(SelectionKey key, long timeout);

    void addReadTimeoutHandler(SelectionKey key, long timeout);

    void addWriteTimeoutHandler(SelectionKey key, long timeout);

    void fire();

    void fire(long time);
}
