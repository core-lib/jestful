package org.qfox.jestful.client.cache;


import org.qfox.jestful.core.Action;

import java.io.IOException;

public interface CacheManager extends CacheStatistics {

    Cache find(String key, Action action) throws IOException;

    Cache save(String key, NegotiatedRequest request, NegotiatedResponse response) throws IOException;

}
