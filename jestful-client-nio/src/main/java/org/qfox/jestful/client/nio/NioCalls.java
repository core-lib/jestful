package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class NioCalls {
    private final Object lock = new Object();
    private final List<NioCall> calls = new LinkedList<NioCall>();
    private final Selector selector;

    public NioCalls(Selector selector) {
        this.selector = selector;
    }

    public void offer(SocketAddress channel, Action attachment) {
        synchronized (lock) {
            calls.add(new NioCall(channel, attachment));
            selector.wakeup();
        }
    }

    public void foreach(NioConsumer consumer) {
        synchronized (lock) {
            Iterator<NioCall> iterator = calls.iterator();
            while (iterator.hasNext()) {
                NioCall call = iterator.next();
                iterator.remove();
                consumer.consume(call.address, call.action);
            }
        }
    }

    public static class NioCall {
        private final SocketAddress address;
        private final Action action;

        public NioCall(SocketAddress address, Action action) {
            this.address = address;
            this.action = action;
        }

    }

    public interface NioConsumer {

        void consume(SocketAddress address, Action attachment);

    }

}
