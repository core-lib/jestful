package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class NioCalls {
    private final Object lock = new Object();
    private final List<Action> calls = new LinkedList<Action>();
    private final Selector selector;

    public NioCalls(Selector selector) {
        this.selector = selector;
    }

    public void offer(Action action) {
        synchronized (lock) {
            calls.add(action);
            selector.wakeup();
        }
    }

    public void foreach(NioConsumer consumer) {
        synchronized (lock) {
            Iterator<Action> iterator = calls.iterator();
            while (iterator.hasNext()) {
                Action action = iterator.next();
                iterator.remove();
                consumer.consume(action);
            }
        }
    }

    public interface NioConsumer {

        void consume(Action action);

    }

}
