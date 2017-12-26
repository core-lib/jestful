package org.qfox.jestful.client.cache.impl.http;

import java.lang.annotation.Annotation;

/**
 * HTTP cache directive resolver for method's parameter which annotated with a cache directive annotation
 *
 * @param <A> cache directive annotation
 */
public interface HttpCacheDirectiveResolver<A extends Annotation> {

    /**
     * resolve a parameter value to a http cache directive value if returns {@code null} that indicated this directive will not be presented
     *
     * @param annotation the parameter cache directive annotation
     * @param value      the parameter value
     * @return http cache directive value
     */
    String resolve(A annotation, Object value);

}
