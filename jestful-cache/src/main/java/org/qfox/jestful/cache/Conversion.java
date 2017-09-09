package org.qfox.jestful.cache;

/**
 * Created by yangchangpei on 17/9/5.
 */
public interface Conversion {

    String convert(Object source);

    <T, C extends Converter<T>> C construct(Class<C> clazz);

}
