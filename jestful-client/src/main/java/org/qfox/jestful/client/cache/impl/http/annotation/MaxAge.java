package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.HttpCacheConstants;
import org.qfox.jestful.client.cache.impl.http.IntegralHttpCacheDirectiveResolver;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CacheDirective(type = {Byte.class, Short.class, Integer.class, Long.class}, name = HttpCacheConstants.MAX_AGE, resolver = IntegralHttpCacheDirectiveResolver.class)
public @interface MaxAge {
}
