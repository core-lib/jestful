package org.qfox.jestful.client;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.converter.StringConverter;
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
 * @date 2016年4月28日 下午7:59:19
 *
 * @since 1.0.0
 */
public class HeaderParameterProcessor implements Actor, Initialable {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String charset = action.getCharset();
		Parameter[] parameters = action.getParameters();
		flag: for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.HEADER) {
				continue;
			}
			for (StringConverter<?> converter : converters) {
				if (converter.support(parameter) == false) {
					continue;
				}
				@SuppressWarnings("unchecked")
				String value = ((StringConverter<Object>) converter).convert(parameter, parameter.getValue());
				String regex = parameter.getRegex();
				if (regex != null && value.matches(regex) == false) {
					throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
				}
				String name = parameter.getName();
				request.setRequestHeader(URLEncoder.encode(name, charset), URLEncoder.encode(value, charset));
				continue flag;
			}
			throw new NoSuchConverterException(parameter);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
