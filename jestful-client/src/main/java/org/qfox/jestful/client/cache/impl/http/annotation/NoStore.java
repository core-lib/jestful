package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.HttpCacheConstants;
import org.qfox.jestful.client.cache.impl.http.resolver.NoStoreHttpCacheDirectiveResolver;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CacheDirective(type = Boolean.class, name = HttpCacheConstants.NO_STORE, resolver = NoStoreHttpCacheDirectiveResolver.class)
public @interface NoStore {
}
