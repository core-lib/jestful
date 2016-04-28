package org.qfox.jestful.server.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
 * @date 2016年4月20日 上午11:03:06
 *
 * @since 1.0.0
 */
public class CookieParameterResolver implements Actor, Initialable {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();
	private boolean caseInsensitive = true;

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		if (request instanceof HttpServletRequest == false) {
			return action.execute();
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Parameter[] parameters = action.getParameters();
		flag: for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.COOKIE) {
				continue;
			}
			Cookie[] cookies = httpServletRequest.getCookies();
			for (Cookie cookie : cookies) {
				if (caseInsensitive ? cookie.getName().equalsIgnoreCase(parameter.getName()) == false : cookie.getName().equals(parameter.getName()) == false) {
					continue;
				}
				String source = cookie.getValue();
				for (StringConverter<?> converter : converters) {
					if (converter.support(parameter)) {
						Object value = converter.convert(parameter, source);
						parameter.setValue(value);
						continue flag;
					}
				}
				throw new NoSuchConverterException(parameter);
			}
		}
		return action.execute();
	}

	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
