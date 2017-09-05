package org.qfox.jestful.cache;

/**
 * Created by yangchangpei on 17/9/5.
 */
public interface Converter<T> {

    String convert(T source, Conversion conversion);

    final class DEFAULT implements Converter<Object> {

        @Override
        public String convert(Object source, Conversion conversion) {
            throw new IllegalStateException();
        }

    }

}
