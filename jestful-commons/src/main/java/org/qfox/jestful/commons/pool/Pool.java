package org.qfox.jestful.commons.pool;

public interface Pool<Key, Item> {

    Item acquire(Key key);

    void release(Key key, Item item);

    void destroy();

}
