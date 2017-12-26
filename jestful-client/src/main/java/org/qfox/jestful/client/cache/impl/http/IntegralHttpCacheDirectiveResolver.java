package org.qfox.jestful.client.cache.impl.http;

import java.lang.annotation.Annotation;

public class IntegralHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<Annotation> {

    @Override
    public String resolve(Annotation annotation, Object value) {
        return value != null ? String.valueOf(value) : null;
    }

}
