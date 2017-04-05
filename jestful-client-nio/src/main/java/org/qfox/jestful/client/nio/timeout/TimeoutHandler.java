package org.qfox.jestful.client.nio.timeout;

import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public abstract class TimeoutHandler implements Comparable<TimeoutHandler> {
    protected final SelectionKey key;
    protected final long timeInvalid;

    protected TimeoutHandler(SelectionKey key, long timeout) {
        this.key = key;
        this.timeInvalid = System.currentTimeMillis() + timeout;
    }

    public abstract boolean isInvalid();

    public boolean isTimeout(long time) {
        return time > timeInvalid;
    }

    public abstract void doTimeout();

    protected SocketTimeoutException toTimeoutException(String message) {
        try {
            throw message == null ? new SocketTimeoutException() : new SocketTimeoutException(message);
        } catch (SocketTimeoutException socketTimeoutException) {
            return socketTimeoutException;
        }
    }

    @Override
    public int compareTo(TimeoutHandler o) {
        return timeInvalid < o.timeInvalid ? -1 : 1;
    }
}
