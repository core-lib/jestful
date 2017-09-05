package org.qfox.jestful.cache;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/5.
 */
public interface Generator {

    String generate(Object object, Method method, Object... args);

    final class DEFAULT implements Generator {

        @Override
        public String generate(Object object, Method method, Object... args) {
            throw new IllegalStateException();
        }
    }

}
