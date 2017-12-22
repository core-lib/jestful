package org.qfox.jestful.client.cache;


import java.io.IOException;

public interface CacheManager extends CacheStatistics {

    Cache find(String key) throws IOException;

    Cache save(String key, NegotiatedRequest request, NegotiatedResponse response) throws IOException;

}
