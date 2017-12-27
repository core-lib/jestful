package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.HttpCacheConstants;
import org.qfox.jestful.client.cache.impl.http.resolver.OnlyIfCachedHttpCacheDirectiveResolver;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CacheDirective(type = Boolean.class, name = HttpCacheConstants.ONLY_IF_CACHED, resolver = OnlyIfCachedHttpCacheDirectiveResolver.class)
public @interface OnlyIfCached {
}
