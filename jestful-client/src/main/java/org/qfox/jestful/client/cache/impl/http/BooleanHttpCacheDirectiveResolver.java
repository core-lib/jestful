package org.qfox.jestful.client.cache.impl.http;

import java.lang.annotation.Annotation;

public class BooleanHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<Annotation> {

    @Override
    public String resolve(Annotation annotation, Object value) {
        return Boolean.TRUE.equals(value) ? "" : null;
    }
}
