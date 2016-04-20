package org.qfox.jestful.server.resolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;

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
public class CookieParameterResolver implements Actor {

	public Object react(Action action) throws Exception {
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.COOKIE) {
				continue;
			}
			Request request = action.getRequest();
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				Cookie[] cookies = httpServletRequest.getCookies();
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(parameter.getName()) == false) {
						continue;
					}
					if (parameter.getKlass() == String.class) {
						parameter.setValue(cookie.getValue());
						break;
					}
					if (parameter.getKlass() == Cookie.class) {
						parameter.setValue(cookie);
						break;
					}
				}
			}
		}
		return action.execute();
	}

}
