package org.qfox.jestful.client.cache;

public interface DataStorage {

    Data get(String key);

    Data alloc(String key);

}
