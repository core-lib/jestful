package org.qfox.jestful.client.nio.timeout;

import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public abstract class TimeoutHandler implements Comparable<TimeoutHandler> {
    protected final SelectionKey key;
    protected final long timeExpired;

    protected TimeoutHandler(SelectionKey key, long timeout) {
        this.key = key;
        this.timeExpired = System.currentTimeMillis() + timeout;
    }

    public boolean isValid() {
        return key.isValid();
    }

    public boolean isTimeout(long time) {
        return time > timeExpired;
    }

    public void doTimeout() {
        key.cancel();
    }

    protected SocketTimeoutException toTimeoutException(String message) {
        try {
            throw message == null ? new SocketTimeoutException() : new SocketTimeoutException(message);
        } catch (SocketTimeoutException socketTimeoutException) {
            return socketTimeoutException;
        }
    }

    @Override
    public int compareTo(TimeoutHandler o) {
        return timeExpired < o.timeExpired ? -1 : 1;
    }
}
