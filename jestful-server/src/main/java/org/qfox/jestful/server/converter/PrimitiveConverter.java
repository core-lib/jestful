package org.qfox.jestful.server.converter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrimitiveConverter implements Converter {
	private final List<Class<?>> primaries = new ArrayList<Class<?>>();

	{
		primaries.add(boolean.class);
		primaries.add(byte.class);
		primaries.add(short.class);
		primaries.add(char.class);
		primaries.add(int.class);
		primaries.add(long.class);
		primaries.add(float.class);
		primaries.add(double.class);
	}

	public boolean supports(Class<?> clazz) {
		return primaries.contains(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map, ConversionProvider provider) throws ConverterException {
		String[] values = map.get(name);
		String value = values != null && values.length > 0 ? values[0] : "0";
		Object result = null;
		switch (primaries.indexOf(clazz)) {
		case 0:
			result = value.equals("0") ? Boolean.FALSE : Boolean.valueOf(value);
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
		default:
			throw new ConverterException("can not converter class " + clazz + " with " + this.getClass(), name, clazz, map, provider);
		}
		return (T) result;
	}

	public boolean supports(ParameterizedType type) {
		return false;
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map, ConversionProvider provider) throws ConverterException {
		throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
	}

}
