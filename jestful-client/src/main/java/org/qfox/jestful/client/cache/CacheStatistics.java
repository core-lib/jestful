package org.qfox.jestful.client.cache;

public interface CacheStatistics {

    void hit();

    long hits();

    void miss();

    long misses();

    void update();

    long updates();

}
