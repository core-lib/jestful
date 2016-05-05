package org.qfox.jestful.client.processor;

import java.net.URLEncoder;
import java.util.List;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
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
 * @date 2016年4月28日 下午8:12:17
 *
 * @since 1.0.0
 */
public class CookieParameterProcessor implements Actor, Initialable {
	private StringConversion cookieStringConversion;

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String cookie = request.getRequestHeader("Cookie");
		cookie = cookie != null ? cookie : "";
		String charset = action.getCharset();
		List<Parameter> parameters = action.getParameters().all(Position.COOKIE);
		for (Parameter parameter : parameters) {
			String[] values = cookieStringConversion.convert(parameter);
			for (int i = 0; values != null && i < values.length; i++) {
				String value = values[i];
				String regex = parameter.getRegex();
				if (regex != null && value.matches(regex) == false) {
					throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
				}
				String name = parameter.getName();
				cookie += (cookie.isEmpty() ? "" : "; ") + URLEncoder.encode(name, charset) + "=" + URLEncoder.encode(value, charset);
			}
		}
		request.setRequestHeader("Cookie", cookie.isEmpty() ? null : cookie);
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		this.cookieStringConversion = beanContainer.get(StringConversion.class);
	}

}
