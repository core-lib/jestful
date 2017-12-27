package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.MaxStale;

public class MaxStaleHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<MaxStale> {

    @Override
    public String resolve(MaxStale annotation, Object value) {
        return value == null || Long.valueOf(value.toString()) < 0 ? null : Long.valueOf(value.toString()) > 0 ? value.toString() : "";
    }

}
