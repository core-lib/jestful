package org.qfox.jestful.client.cache;


import java.io.IOException;

public interface CacheManager {

    Cache find(String key) throws IOException;

    Cache save(String key, NegotiatedRequest request, NegotiatedResponse response) throws IOException;

}
