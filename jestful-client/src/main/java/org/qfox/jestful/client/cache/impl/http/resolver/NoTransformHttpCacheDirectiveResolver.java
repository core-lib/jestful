package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.NoTransform;

public class NoTransformHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<NoTransform> {

    @Override
    public String resolve(NoTransform annotation, Object value) {
        return Boolean.TRUE.equals(value) ? "" : null;
    }
}
