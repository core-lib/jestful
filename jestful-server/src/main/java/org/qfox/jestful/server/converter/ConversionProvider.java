package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public interface ConversionProvider {

	Object convert(String name, Type type, Map<String, String[]> map) throws ConversionException;

	<T> T convert(String name, Class<T> clazz, Map<String, String[]> map) throws ConversionException;

	Object convert(String name, ParameterizedType type, Map<String, String[]> map) throws ConversionException;

}
