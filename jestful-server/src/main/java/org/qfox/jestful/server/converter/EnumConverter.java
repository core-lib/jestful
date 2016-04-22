package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class EnumConverter implements Converter {

	public boolean supports(Class<?> clazz) {
		return clazz.isEnum();
	}

	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map, ConversionProvider provider) throws ConversionException {
		String[] values = map.get(name);
		String value = values != null && values.length > 0 ? values[0] : null;
		if (value == null) {
			return null;
		}
		try {
			Object result = clazz.getMethod("valueOf", String.class).invoke(null, value);
			return clazz.cast(result);
		} catch (Exception e) {
			throw new IncompatibleConversionException(e, name, clazz, map, provider);
		}
	}

	public boolean supports(ParameterizedType type) {
		return false;
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map, ConversionProvider provider) throws ConversionException {
		throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
	}

}
