package org.qfox.jestful.client.cache.impl.http.annotation;

import org.qfox.jestful.client.cache.impl.http.ExtendedHttpCacheDirectiveResolver;

import java.lang.annotation.*;

/**
 * cache extension directive
 * <p>
 * cache-request-directive =
 * "no-cache" ; Section 14.9.1
 * | "no-store" ; Section 14.9.2
 * | "max-age" "=" delta-seconds ; Section 14.9.3, 14.9.4
 * | "max-stale" [ "=" delta-seconds ] ; Section 14.9.3
 * | "min-fresh" "=" delta-seconds ; Section 14.9.3
 * | "no-transform" ; Section 14.9.5
 * | "only-if-cached" ; Section 14.9.4
 * | cache-extension ; Section 14.9.6
 * <p>
 * the cache-extension specification
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@CacheDirective(type = String.class, name = "", resolver = ExtendedHttpCacheDirectiveResolver.class)
public @interface CacheExtension {

    /**
     * @return extension key
     */
    String key() default "";

    /**
     * @return extension value if {@link CacheExtension#key()} is empty this value is the extension key
     */
    String value() default "";

    /**
     * @return whether the extension value should be quoted
     */
    boolean quoted() default false;

}
