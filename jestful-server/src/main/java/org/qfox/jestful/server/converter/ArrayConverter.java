package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.commons.Utils;

public class ArrayConverter implements Converter {

	public boolean supports(Class<?> clazz) {
		return clazz.isArray();
	}

	public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
		Object array = Array.newInstance(clazz.getComponentType(), 0);
		for (String key : map.keySet()) {
			if (key.equals(name) || key.startsWith(name + ".")) {
				String[] values = map.get(key) != null ? map.get(key).clone() : new String[0];
				for (int i = 0; i < values.length; i++) {
					Map<String, String[]> _map = new HashMap<String, String[]>();
					_map.put(name, Utils.copyOfRange(values, i, i + 1));
					Object object = provider.convert(name, clazz.getComponentType(), decoded, charset, _map);
					int index = Array.getLength(array);
					Object _array = Array.newInstance(clazz.getComponentType(), index + 1);
					System.arraycopy(array, 0, _array, 0, index);
					array = _array;
					Array.set(array, index, object);
				}
			}
		}
		return clazz.cast(array);
	}

	public boolean supports(ParameterizedType type) {
		return false;
	}

	public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
		throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
	}
}
