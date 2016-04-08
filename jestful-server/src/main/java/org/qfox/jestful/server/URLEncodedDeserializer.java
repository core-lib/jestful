package org.qfox.jestful.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Deserializer;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Source;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.Converter;
import org.qfox.jestful.server.exception.UnconvertableParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
 * @date 2016年4月8日 下午5:23:18
 *
 * @since 1.0.0
 */
public class URLEncodedDeserializer implements Deserializer, ApplicationContextAware, ConversionProvider {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	public final Set<Converter> converters = new LinkedHashSet<Converter>();

	public String getContentType() {
		return "application/x-www-url-encoded";
	}

	public void deserialize(Action action, InputStream in) throws IOException {
		Map<String, String[]> map = new HashMap<String, String[]>();
		
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			String charset = action.getCharset();
			String[] pairs = line.split("&+");
			for (String pair : pairs) {
				String[] keyvalue = pair.split("=+");
				String key = URLDecoder.decode(keyvalue[0], charset);
				String value = keyvalue.length > 1 ? URLDecoder.decode(keyvalue[1], charset) : "";
				if (map.containsKey(key) == false) {
					map.put(key, new String[0]);
				}
				String[] values = map.get(key);
				values = Arrays.copyOf(values, values.length + 1);
				values[values.length - 1] = value;
				map.put(key, values);
			}
		}
		
		Parameter[] parameters = action.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.getSource() != Source.FIELD) {
				continue;
			}
			String name = parameter.getName();
			Type type = parameter.getType();
			Object value = convert(name, type, map);
			parameter.setValue(value);
		}
	}

	public Object convert(String name, Type type, Map<String, String[]> map) throws UnconvertableParameterException {
		if (type instanceof Class<?>) {
			return convert(name, (Class<?>) type, map);
		}
		if (type instanceof ParameterizedType) {
			return convert(name, (ParameterizedType) type, map);
		}
		throw new UnconvertableParameterException("unsupported type " + type, name, type, map, this);
	}

	public <T> T convert(String name, Class<T> clazz, Map<String, String[]> map) throws UnconvertableParameterException {
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
		throw new UnconvertableParameterException("unsupported clazz " + clazz, name, clazz, map, this);
	}

	public Object convert(String name, ParameterizedType type, Map<String, String[]> map) throws UnconvertableParameterException {
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
		throw new UnconvertableParameterException("unsupported parameterized type " + type, name, type, map, this);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, ?> beans = applicationContext.getBeansOfType(Converter.class);
		for (Object bean : beans.values()) {
			converters.add((Converter) bean);
		}
	}
}
