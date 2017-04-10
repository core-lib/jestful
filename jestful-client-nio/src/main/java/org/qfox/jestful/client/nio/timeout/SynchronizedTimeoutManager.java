package org.qfox.jestful.client.nio.timeout;

import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/4/10.
 */
public class SynchronizedTimeoutManager implements TimeoutManager {
    private final TimeoutManager timeoutManager;

    public SynchronizedTimeoutManager(TimeoutManager timeoutManager) {
        this.timeoutManager = timeoutManager;
    }

    @Override
    public synchronized void addConnTimeoutHandler(SelectionKey key, long timeout) {
        timeoutManager.addConnTimeoutHandler(key, timeout);
    }

    @Override
    public synchronized void addRecvTimeoutHandler(SelectionKey key, long timeout) {
        timeoutManager.addRecvTimeoutHandler(key, timeout);
    }

    @Override
    public synchronized void addSendTimeoutHandler(SelectionKey key, long timeout) {
        timeoutManager.addSendTimeoutHandler(key, timeout);
    }

    @Override
    public synchronized void fire() {
        timeoutManager.fire();
    }

    @Override
    public synchronized void fire(long time) {
        timeoutManager.fire(time);
    }

}
