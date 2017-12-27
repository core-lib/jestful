package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.MaxAge;

public class MaxAgeHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<MaxAge> {

    @Override
    public String resolve(MaxAge annotation, Object value) {
        return value == null || Long.valueOf(value.toString()) < 0 ? null : value.toString();
    }

}
