package org.qfox.jestful.server;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.Converter;
import org.qfox.jestful.server.converter.UnsupportedConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @date 2016年4月22日 下午4:10:50
 *
 * @since 1.0.0
 */
public class JestfulConversionProvider implements ConversionProvider, Initialable {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Set<Converter> converters = new LinkedHashSet<Converter>();

	public Object convert(String name, Type type, Map<String, String[]> map) throws ConversionException {
		if (type instanceof Class<?>) {
			return convert(name, (Class<?>) type, map);
		}
		if (type instanceof ParameterizedType) {
			return convert(name, (ParameterizedType) type, map);
		}
		throw new UnsupportedConversionException("unsupported type " + type, name, type, map, this);
	}

	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map) throws ConversionException {
		for (Converter converter : converters) {
			if (converter.supports(clazz)) {
				try {
					return converter.convert(name, clazz, map, this);
				} catch (Exception e) {
					logger.warn("can not convert class {} with name {} using parameters {}", clazz, name, map, e);
					return null;
				}
			}
		}
		throw new UnsupportedConversionException("unsupported clazz " + clazz, name, clazz, map, this);
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map) throws ConversionException {
		for (Converter converter : converters) {
			if (converter.supports(type)) {
				try {
					return converter.convert(name, type, map, this);
				} catch (Exception e) {
					logger.warn("can not convert parameterized type {} with name {} using parameters {}", type, name, map, e);
					return null;
				}
			}
		}
		throw new UnsupportedConversionException("unsupported parameterized type " + type, name, type, map, this);
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(Converter.class);
		for (Object bean : beans.values()) {
			converters.add((Converter) bean);
		}
	}

}
