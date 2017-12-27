package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.CacheStatistics;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultCacheStatistics implements CacheStatistics {
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong updates = new AtomicLong();

    @Override
    public void hit() {
        hits.incrementAndGet();
    }

    @Override
    public long hits() {
        return hits.get();
    }

    @Override
    public void miss() {
        misses.incrementAndGet();
    }

    @Override
    public long misses() {
        return misses.get();
    }

    @Override
    public void update() {
        updates.incrementAndGet();
    }

    @Override
    public long updates() {
        return updates.get();
    }
}
