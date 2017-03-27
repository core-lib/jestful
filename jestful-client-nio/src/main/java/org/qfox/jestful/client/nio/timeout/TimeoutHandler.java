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

    public boolean isInvalid(long time) {
        return time > timeInvalid;
    }

    protected SocketTimeoutException wrapSocketTimeoutException() {
        return wrapSocketTimeoutException(null);
    }

    protected SocketTimeoutException wrapSocketTimeoutException(String msg) {
        try {
            throw msg == null ? new SocketTimeoutException() : new SocketTimeoutException(msg);
        } catch (SocketTimeoutException ste) {
            return ste;
        }
    }

    public abstract void doInvalid();

    public SelectionKey getKey() {
        return key;
    }

    public long getTimeInvalid() {
        return timeInvalid;
    }

    @Override
    public int compareTo(TimeoutHandler o) {
        return timeInvalid < o.timeInvalid ? -1 : 1;
    }
}
