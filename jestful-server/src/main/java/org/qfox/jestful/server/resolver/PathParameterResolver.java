package org.qfox.jestful.server.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
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
 * @date 2016年4月7日 下午8:36:13
 *
 * @since 1.0.0
 */
public class PathParameterResolver implements Actor, Initialable {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();

	public Object react(Action action) throws Exception {
		String URI = action.getURI();
		List<Parameter> parameters = action.getParameters().all(Position.PATH);
		Pattern pattern = action.getPattern();
		Matcher matcher = pattern.matcher(URI);
		matcher.find();
		flag: for (Parameter parameter : parameters) {
			int group = parameter.getGroup();
			if (group <= 0) {
				continue;
			}
			String source = matcher.group(group);
			for (StringConverter<?> converter : converters) {
				if (converter.support(parameter)) {
					Object value = converter.convert(parameter, source);
					parameter.setValue(value);
					continue flag;
				}
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
