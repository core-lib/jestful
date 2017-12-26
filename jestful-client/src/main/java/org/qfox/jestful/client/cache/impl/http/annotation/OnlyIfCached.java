package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.BooleanHttpCacheDirectiveResolver;
import org.qfox.jestful.client.cache.impl.http.HttpCacheConstants;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CacheDirective(type = Boolean.class, name = HttpCacheConstants.ONLY_IF_CACHED, resolver = BooleanHttpCacheDirectiveResolver.class)
public @interface OnlyIfCached {
}
