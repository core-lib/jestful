package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public interface ConversionProvider {

    Object convert(String name, Type type, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException;

    <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException;

    Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException;

    Object extend(String name, Class<?> clazz, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException;

}
