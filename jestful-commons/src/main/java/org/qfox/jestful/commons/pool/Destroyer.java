package org.qfox.jestful.commons.pool;

public interface Destroyer<Item> {

    void destroy(Item item);

}
