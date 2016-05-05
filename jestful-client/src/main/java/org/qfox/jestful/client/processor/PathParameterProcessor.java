package org.qfox.jestful.client.processor;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.converter.StringConversion;

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
	private StringConversion pathStringConversion;

	public Object react(Action action) throws Exception {
		String expression = action.getResource().getExpression();
		String definition = action.getMapping().getDefinition();
		String URI = expression + definition;
		String charset = action.getCharset();
		List<Parameter> parameters = action.getParameters().all(Position.PATH);
		for (Parameter parameter : parameters) {
			String[] values = pathStringConversion.convert(parameter);
			String value = values[0];
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
		}
		action.setURI(URI);
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		this.pathStringConversion = beanContainer.get(StringConversion.class);
	}

}
