package org.qfox.jestful.server.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.annotation.Argument.Position;
import org.qfox.jestful.core.converter.StringConverter;
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
 * @date 2016年4月7日 下午8:36:13
 *
 * @since 1.0.0
 */
public class PathParameterResolver implements Actor, ApplicationContextAware {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();

	public Object react(Action action) throws Exception {
		String URI = action.getURI();
		Parameter[] parameters = action.getParameters();
		Pattern pattern = action.getPattern();
		Matcher matcher = pattern.matcher(URI);
		matcher.find();
		flag: for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			int group = parameter.getGroup();
			if (parameter.getPosition() != Position.PATH || group <= 0) {
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
		}
		return action.execute();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, ?> beans = applicationContext.getBeansOfType(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
