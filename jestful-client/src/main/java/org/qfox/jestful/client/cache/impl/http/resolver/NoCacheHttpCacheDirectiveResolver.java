package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.NoCache;

public class NoCacheHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<NoCache> {

    @Override
    public String resolve(NoCache annotation, Object value) {
        return Boolean.TRUE.equals(value) ? "" : null;
    }
}
