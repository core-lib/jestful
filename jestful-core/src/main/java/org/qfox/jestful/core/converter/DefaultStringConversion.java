package org.qfox.jestful.core.converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.NoSuchConverterException;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月5日 上午10:53:58
 *
 * @since 1.0.0
 */
public class DefaultStringConversion implements StringConversion, Initialable {
	private final List<StringConverter<Object>> converters = new ArrayList<StringConverter<Object>>();

	@SuppressWarnings("unchecked")
	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<Object>) bean);
		}
	}

	public boolean supports(Parameter parameter) {
		Class<?> klass = parameter.getKlass();
		if (klass.isArray()) {
			klass = klass.getComponentType();
		}
		for (StringConverter<?> converter : converters) {
			if (converter.support(klass)) {
				return true;
			}
		}
		return false;
	}

	public void convert(Parameter parameter, String source) throws NoSuchConverterException {
		Class<?> klass = parameter.getKlass();
		if (klass.isArray()) {
			klass = klass.getComponentType();
			for (StringConverter<?> converter : converters) {
				if (converter.support(klass)) {
					Object value = converter.convert(klass, source);
					Object array = parameter.getValue();
					if (array == null) {
						array = Array.newInstance(klass, 1);
						Array.set(array, 0, value);
						parameter.setValue(array);
					} else if (array.getClass().isArray()) {
						int length = Array.getLength(array);
						Object _array = Array.newInstance(klass, length + 1);
						System.arraycopy(array, 0, _array, 0, length);
						Array.set(_array, length, value);
						parameter.setValue(_array);
					} else {
						throw new IllegalStateException("expecting parameter value is type of array but got " + array.getClass());
					}
					return;
				}
			}
			throw new NoSuchConverterException(parameter);
		} else {
			for (StringConverter<?> converter : converters) {
				if (converter.support(klass)) {
					Object value = converter.convert(klass, source);
					parameter.setValue(value);
					return;
				}
			}
			throw new NoSuchConverterException(parameter);
		}
	}

	public String[] convert(Parameter parameter) throws NoSuchConverterException {
		Class<?> klass = parameter.getKlass();
		if (klass.isArray()) {
			klass = klass.getComponentType();
			Object array = parameter.getValue();
			if (array == null) {
				return null;
			} else if (array.getClass().isArray()) {
				StringConverter<Object> converter = null;
				for (StringConverter<Object> c : converters) {
					if (c.support(klass)) {
						converter = c;
						break;
					}
				}
				if (converter == null) {
					throw new NoSuchConverterException(parameter);
				}
				int length = Array.getLength(array);
				String[] targets = new String[length];
				for (int index = 0; index < length; index++) {
					Object element = Array.get(array, index);
					String target = converter.convert(klass, element);
					targets[index] = target;
				}
				return targets;
			} else {
				throw new IllegalStateException("expecting parameter value is type of array but got " + array.getClass());
			}
		} else {
			for (StringConverter<Object> converter : converters) {
				if (converter.support(klass)) {
					Object source = parameter.getValue();
					String target = converter.convert(klass, source);
					return new String[] { target };
				}
			}
			throw new NoSuchConverterException(parameter);
		}
	}
}
