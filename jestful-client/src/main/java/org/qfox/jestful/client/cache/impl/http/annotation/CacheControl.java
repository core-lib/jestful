package org.qfox.jestful.client.cache.impl.http.annotation;

import java.lang.annotation.*;

/**
 * cache request directive
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
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CacheControl {

    /**
     * @return if no-cache
     */
    boolean noCache() default false;

    /**
     * @return if no-store
     */
    boolean noStore() default false;

    /**
     * @return it would be ignored if value is negative
     */
    long maxAge() default -1;

    /**
     * @return it would be ignored if value is negative if {@code 0} the delta-seconds would be ignored
     */
    long maxStale() default -1;

    /**
     * @return it would be ignored if value is negative
     */
    long minFresh() default -1;

    /**
     * @return if no-transform
     */
    boolean noTransform() default false;

    /**
     * @return if only-if-cached
     */
    boolean onlyIfCached() default false;

    /**
     * @return cache-extensions
     */
    CacheExtension[] cacheExtensions() default {};

}
