package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.CacheExtension;

public class CacheExtensionHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<CacheExtension> {

    @Override
    public String resolve(CacheExtension annotation, Object value) {
        return value != null ? annotation.quoted() ? "\"" + String.valueOf(value).trim() + "\"" : String.valueOf(value).trim() : null;
    }

}
