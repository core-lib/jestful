package org.qfox.jestful.core.converter;

import java.math.BigInteger;

/**
 * Created by yangchangpei on 17/5/12.
 */
public class BigIntegerStringConverter implements StringConverter<BigInteger> {

    public boolean support(Class<?> klass) {
        return BigInteger.class.isAssignableFrom(klass);
    }

    public String convert(Class<?> klass, BigInteger source) {
        return source.toString();
    }

    public BigInteger convert(Class<?> klass, String source) {
        try {
            return (BigInteger) klass.getConstructor(String.class).newInstance(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
