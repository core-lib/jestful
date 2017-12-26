package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.impl.http.annotation.CacheExtension;

public class ExtendedHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<CacheExtension> {

    @Override
    public String resolve(CacheExtension annotation, Object value) {
        return value != null ? annotation.quoted() ? "\"" + String.valueOf(value).trim() + "\"" : String.valueOf(value).trim() : null;
    }

}
