package org.qfox.jestful.client.nio;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class Registrations {
    private final Object lock = new Object();
    private final List<Registration> registrations = new LinkedList<Registration>();
    private final Selector selector;

    public Registrations(Selector selector) {
        this.selector = selector;
    }

    public void register(SocketChannel channel, int options) {
        register(channel, options, null);
    }

    public void register(SocketChannel channel, int options, Object attachment) {
        synchronized (lock) {
            registrations.add(new Registration(channel, options, attachment));
            selector.wakeup();
        }
    }

    public void foreach(Consumer consumer) {
        synchronized (lock) {
            Iterator<Registration> iterator = registrations.iterator();
            while (iterator.hasNext()) {
                Registration registration = iterator.next();
                consumer.consume(registration.channel, registration.options, registration.attachment);
                iterator.remove();
            }
        }
    }

    public static class Registration {
        private final SocketChannel channel;
        private final int options;
        private final Object attachment;

        public Registration(SocketChannel channel, int options, Object attachment) {
            this.channel = channel;
            this.options = options;
            this.attachment = attachment;
        }

    }

    public interface Consumer {

        void consume(SocketChannel channel, int options, Object attachment);

    }

}
