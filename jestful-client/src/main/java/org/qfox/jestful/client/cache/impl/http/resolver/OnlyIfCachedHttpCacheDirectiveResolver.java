package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.OnlyIfCached;

public class OnlyIfCachedHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<OnlyIfCached> {

    @Override
    public String resolve(OnlyIfCached annotation, Object value) {
        return Boolean.TRUE.equals(value) ? "" : null;
    }
}
