package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.Data;
import org.qfox.jestful.client.cache.DataStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BasicDataStorage implements DataStorage {
    private final ConcurrentMap<String, BasicData> storage = new ConcurrentHashMap<String, BasicData>();

    @Override
    public Data get(String key) {
        if (key == null) throw new NullPointerException();
        return storage.get(key);
    }

    @Override
    public Data alloc(String key) {
        if (key == null) throw new NullPointerException();
        return new BasicData(key);
    }

    private class BasicData implements Data {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final String key;
        private volatile byte[] bytes;

        BasicData(String key) {
            this.key = key;
        }

        @Override
        public void read(Reader reader) throws IOException {
            try {
                rwLock.readLock().lock();
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                reader.read(in);
            } finally {
                rwLock.readLock().unlock();
            }
        }

        @Override
        public void write(Writer writer) throws IOException {
            try {
                rwLock.writeLock().lock();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                writer.write(out);
                bytes = out.toByteArray();
                storage.put(key, this);
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        @Override
        public void free() throws IOException {
            try {
                rwLock.writeLock().lock();
                storage.remove(key, this);
                bytes = null;
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }

}
