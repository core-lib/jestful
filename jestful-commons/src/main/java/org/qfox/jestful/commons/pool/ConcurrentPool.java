package org.qfox.jestful.commons.pool;

import org.qfox.jestful.commons.Equivocal;
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
    private final ConcurrentMap<Key, Queue<Equivocal<Item>>> pool = new ConcurrentHashMap<Key, Queue<Equivocal<Item>>>();
    private final Producer<Key, Item> producer;
    private final Validator<Item> validator;
    private final Destroyer<Item> destroyer;

    public ConcurrentPool() {
        this(new Producer<Key, Item>() {
            @Override
            public Item produce(Key key) {
                return null;
            }
        }, new Validator<Item>() {
            @Override
            public boolean validate(Item item) {
                return true;
            }
        }, new Destroyer<Item>() {
            @Override
            public void destroy(Item item) {

            }
        });
    }

    public ConcurrentPool(Producer<Key, Item> producer, Validator<Item> validator, Destroyer<Item> destroyer) {
        this.producer = producer;
        this.validator = validator;
        this.destroyer = destroyer;
    }

    @Override
    public Item acquire(Key key) {
        while (true) {
            int i = modifying.get();
            if (i < 0) return null;

            if (!modifying.compareAndSet(i, i + 1)) continue;
            try {
                Queue<Equivocal<Item>> queue = pool.get(key);
                Equivocal<Item> equivocal = queue != null ? queue.poll() : null;
                if (equivocal == null) {
                    return producer.produce(key);
                } else if (validator.validate(equivocal.get())) {
                    return equivocal.get();
                } else {
                    destroyer.destroy(equivocal.get());
                    return acquire(key);
                }
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
                Queue<Equivocal<Item>> queue = pool.get(key);
                if (queue == null) {
                    Queue<Equivocal<Item>> old = pool.putIfAbsent(key, queue = new ConcurrentLinkedQueue<Equivocal<Item>>());
                    if (old != null) queue = old;
                }
                if (validator.validate(item)) {
                    queue.offer(Equivocal.of(item));
                } else {
                    destroyer.destroy(item);
                }
                break;
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
        for (Queue<Equivocal<Item>> queue : pool.values()) {
            for (Equivocal<Item> item : queue) destroyer.destroy(item.get());
            queue.clear();
        }
        pool.clear();
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
