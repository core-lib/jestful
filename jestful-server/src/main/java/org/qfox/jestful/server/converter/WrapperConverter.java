package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WrapperConverter implements Converter {
	private final List<Class<?>> wrappers = new ArrayList<Class<?>>();

	{
		wrappers.add(Boolean.class);
		wrappers.add(Byte.class);
		wrappers.add(Short.class);
		wrappers.add(Character.class);
		wrappers.add(Integer.class);
		wrappers.add(Long.class);
		wrappers.add(Float.class);
		wrappers.add(Double.class);
	}

	public boolean supports(Class<?> clazz) {
		return wrappers.contains(clazz);
	}

	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map, ConversionProvider provider) throws ConversionException {
		String[] values = map.get(name);
		String value = values != null && values.length > 0 ? values[0] : null;
		if (value == null) {
			return null;
		}
		Object result = null;
		try {
			switch (wrappers.indexOf(clazz)) {
			case 0:
				result = Boolean.valueOf(value);
				break;
			case 1:
				result = Byte.valueOf(value);
				break;
			case 2:
				result = Short.valueOf(value);
				break;
			case 3:
				result = Character.valueOf(value.charAt(0));
				break;
			case 4:
				result = Integer.valueOf(value);
				break;
			case 5:
				result = Long.valueOf(value);
				break;
			case 6:
				result = Float.valueOf(value);
				break;
			case 7:
				result = Double.valueOf(value);
				break;
			}
		} catch (Exception e) {
			throw new UncompitableConversionException(e, name, clazz, map, provider);
		}
		return clazz.cast(result);
	}

	public boolean supports(ParameterizedType type) {
		return false;
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map, ConversionProvider provider) throws ConversionException {
		throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
	}

}
