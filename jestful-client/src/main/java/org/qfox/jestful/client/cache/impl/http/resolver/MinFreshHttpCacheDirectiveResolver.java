package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.MinFresh;

public class MinFreshHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<MinFresh> {

    @Override
    public String resolve(MinFresh annotation, Object value) {
        return value == null || Long.valueOf(value.toString()) < 0 ? null : value.toString();
    }

}
