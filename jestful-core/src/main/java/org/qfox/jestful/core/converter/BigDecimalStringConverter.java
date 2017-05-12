package org.qfox.jestful.core.converter;

import java.math.BigDecimal;

/**
 * Created by yangchangpei on 17/5/12.
 */
public class BigDecimalStringConverter implements StringConverter<BigDecimal> {

    public boolean support(Class<?> klass) {
        return BigDecimal.class.isAssignableFrom(klass);
    }

    public String convert(Class<?> klass, BigDecimal source) {
        return source.toString();
    }

    public BigDecimal convert(Class<?> klass, String source) {
        try {
            return (BigDecimal) klass.getConstructor(String.class).newInstance(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
