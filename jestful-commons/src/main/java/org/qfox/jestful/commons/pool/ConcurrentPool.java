package org.qfox.jestful.commons.pool;

import org.qfox.jestful.commons.LockBlock;
import org.qfox.jestful.commons.SimpleLock;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentPool<Key, Item> implements Pool<Key, Item> {
    private final AtomicInteger modifying = new AtomicInteger();
    private final SimpleLock lock = new SimpleLock();
    private final ConcurrentMap<Key, Queue<Item>> pool = new ConcurrentHashMap<Key, Queue<Item>>();
    private final Destroyer<Item> destroyer;

    public ConcurrentPool() {
        this(new Destroyer<Item>() {
            @Override
            public void destroy(Item item) {

            }
        });
    }

    public ConcurrentPool(Destroyer<Item> destroyer) {
        this.destroyer = destroyer;
    }

    @Override
    public Item acquire(Key key) {
        while (true) {
            int i = modifying.get();
            if (i < 0) return null;

            if (!modifying.compareAndSet(i, i + 1)) continue;
            try {
                Queue<Item> queue = pool.get(key);
                return queue != null ? queue.poll() : null;
            } finally {
                i = modifying.decrementAndGet();
                if (i < 0) lock.doWithLock(new LockBlock() {
                    @Override
                    public void execute() {
                        lock.openAll();
                    }
                });
            }
        }
    }

    @Override
    public void release(Key key, Item item) {
        while (true) {
            int i = modifying.get();
            if (i < 0) return;

            if (!modifying.compareAndSet(i, i + 1)) continue;
            try {
                Queue<Item> queue = pool.get(key);
                if (queue == null) {
                    Queue<Item> old = pool.putIfAbsent(key, queue = new ConcurrentLinkedQueue<Item>());
                    if (old != null) queue = old;
                }
                queue.offer(item);
                return;
            } finally {
                i = modifying.decrementAndGet();
                if (i < 0) lock.doWithLock(new LockBlock() {
                    @Override
                    public void execute() {
                        lock.openAll();
                    }
                });
            }
        }
    }

    @Override
    public void destroy() {
        int i = modifying.getAndDecrement();
        if (i < 0) return;
        if (i > 0) lock.doWithLock(new LockBlock() {
            @Override
            public void execute() {
                lock.lockOne();
            }
        });
        for (Queue<Item> queue : pool.values()) {
            for (Item item : queue) destroyer.destroy(item);
            queue.clear();
        }
        pool.clear();
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
