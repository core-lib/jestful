package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public interface Converter {

	boolean supports(Class<?> clazz);

	<T> T convert(String name, Class<T> clazz, Map<String, String[]> map, ConversionProvider provider) throws ConversionException;

	boolean supports(ParameterizedType type);

	Object convert(String name, ParameterizedType type, Map<String, String[]> map, ConversionProvider provider) throws ConversionException;

}
