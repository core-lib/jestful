package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.qfox.jestful.server.exception.UnconvertableParameterException;

public class StringConverter implements Converter {

	public boolean supports(Class<?> clazz) {
		return clazz == String.class;
	}

	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map, ConversionProvider provider) throws UnconvertableParameterException {
		String[] values = map.get(name);
		return clazz.cast(values != null && values.length > 0 ? values[0] : null);
	}

	public boolean supports(ParameterizedType type) {
		return false;
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map, ConversionProvider provider) throws UnconvertableParameterException {
		throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
	}

}
