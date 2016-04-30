package org.qfox.jestful.client;

import java.net.URLEncoder;
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
import org.qfox.jestful.core.Parameters;
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
 * @date 2016年4月28日 下午2:47:59
 *
 * @since 1.0.0
 */
public class PathParameterProcessor implements Actor, Initialable {
	private final Pattern pattern = Pattern.compile("\\{[^{}]+?\\}");
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();

	public Object react(Action action) throws Exception {
		String definition = action.getMapping().getDefinition();
		String URI = definition;
		String charset = action.getCharset();
		Parameters parameters = action.getParameters();
		flag: for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.PATH) {
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
				Matcher matcher = pattern.matcher(definition);
				int group = parameter.getGroup();
				for (int i = 0; i < group; i++) {
					matcher.find();
				}
				String variable = matcher.group();
				URI = URI.replace(variable, URLEncoder.encode(value, charset));
				continue flag;
			}
			throw new NoSuchConverterException(parameter);
		}
		action.setURI(URI);
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
