package org.qfox.jestful.commons.pool;

public interface Producer<Key, Item> {

    Item produce(Key key);

}
