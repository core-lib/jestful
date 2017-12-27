package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.resolver.HttpCacheDirectiveResolver;

import java.io.Serializable;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface CacheDirective {

    /**
     * @return allow types
     */
    Class<? extends Serializable>[] type();

    /**
     * @return cache directive name
     */
    String name();

    /**
     * @return cache directive resolver
     */
    Class<? extends HttpCacheDirectiveResolver<?>> resolver();

}
