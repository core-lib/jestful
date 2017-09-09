package org.qfox.jestful.cache;

import org.qfox.jestful.cache.exception.IllegalTypeException;
import org.qfox.jestful.cache.exception.RefreshRequiredException;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/5.
 */
public interface Generator {

    String generate(Object object, Method method, Parameter... parameters) throws IllegalTypeException, RefreshRequiredException;

    final class DEFAULT implements Generator {

        @Override
        public String generate(Object object, Method method, Parameter... parameters) {
            throw new IllegalStateException();
        }
    }

}
