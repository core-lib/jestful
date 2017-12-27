package org.qfox.jestful.client.cache.impl.http.resolver;

import org.qfox.jestful.client.cache.impl.http.annotation.NoStore;

public class NoStoreHttpCacheDirectiveResolver implements HttpCacheDirectiveResolver<NoStore> {

    @Override
    public String resolve(NoStore annotation, Object value) {
        return Boolean.TRUE.equals(value) ? "" : null;
    }
}
