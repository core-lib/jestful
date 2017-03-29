package org.qfox.jestful.client.nio;

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

    public void offer(SocketAddress channel, Object attachment) {
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
                consumer.consume(call.address, call.attachment);
            }
        }
    }

    public static class NioCall {
        private final SocketAddress address;
        private final Object attachment;

        public NioCall(SocketAddress address, Object attachment) {
            this.address = address;
            this.attachment = attachment;
        }

    }

    public interface NioConsumer {

        void consume(SocketAddress address, Object attachment);

    }

}
