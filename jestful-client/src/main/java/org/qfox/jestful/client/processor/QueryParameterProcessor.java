package org.qfox.jestful.client.processor;

import java.net.URLEncoder;
import java.util.List;

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
 * @date 2016年4月28日 下午5:49:14
 *
 * @since 1.0.0
 */
public class QueryParameterProcessor implements Actor, Initialable {
	private StringConversion queryStringConversion;

	public Object react(Action action) throws Exception {
		String query = action.getQuery();
		query = query != null ? query : "";
		String charset = action.getQueryEncodeCharset();
		List<Parameter> parameters = action.getParameters().all(Position.QUERY);
		for (Parameter parameter : parameters) {
			String[] values = queryStringConversion.convert(parameter);
			for (int i = 0; values != null && i < values.length; i++) {
				String value = values[i];
				String regex = parameter.getRegex();
				if (regex != null && value.matches(regex) == false) {
					throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
				}
				String name = parameter.getName();
				query += (query.length() == 0 ? "" : "&") + URLEncoder.encode(name, charset) + "=" + URLEncoder.encode(value, charset);
			}
		}
		action.setQuery(query == null || query.length() == 0 ? null : query);
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		this.queryStringConversion = beanContainer.get(StringConversion.class);
	}

	public StringConversion getQueryStringConversion() {
		return queryStringConversion;
	}

	public void setQueryStringConversion(StringConversion queryStringConversion) {
		this.queryStringConversion = queryStringConversion;
	}

}
