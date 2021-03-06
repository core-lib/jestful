package org.qfox.jestful.client.nio.timeout;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by yangchangpei on 17/3/27.
 */
public class SortedTimeoutManager implements TimeoutManager {
    private Set<TimeoutHandler> handlers = new TreeSet<TimeoutHandler>();

    @Override
    public void addConnTimeoutHandler(SelectionKey key, long timeout) {
        handlers.add(new ConnTimeoutHandler(key, timeout));
    }

    @Override
    public void addRecvTimeoutHandler(SelectionKey key, long timeout) {
        handlers.add(new RecvTimeoutHandler(key, timeout));
    }

    @Override
    public void addSendTimeoutHandler(SelectionKey key, long timeout) {
        handlers.add(new SendTimeoutHandler(key, timeout));
    }

    @Override
    public void fire() {
        fire(System.currentTimeMillis());
    }

    @Override
    public void fire(long time) {
        Iterator<TimeoutHandler> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            TimeoutHandler handler = iterator.next();
            if (handler.isChanged()) {
                iterator.remove();
            } else if (handler.isTimeout(time)) {
                iterator.remove();
                handler.doTimeout();
            } else {
                break;
            }
        }
    }
}
