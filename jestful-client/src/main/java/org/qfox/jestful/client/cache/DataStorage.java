package org.qfox.jestful.client.cache;

import java.io.IOException;

public interface DataStorage {

    Data get(String key) throws IOException;

    Data alloc(String key) throws IOException;

}
