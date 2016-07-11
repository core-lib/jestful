package org.qfox.jestful.server.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
 * @date 2016年7月11日 下午4:05:27
 *
 * @since 1.0.0
 */
public class SessionParameterResolver implements Actor {

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		if (request instanceof HttpServletRequest == false) {
			return action.execute();
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession();
		if (session == null) {
			return action.execute();
		}
		List<Parameter> parameters = action.getParameters().all(Position.SESSION);
		for (Parameter parameter : parameters) {
			String key = parameter.getName();
			Object value = session.getAttribute(key);
			parameter.setValue(value);
		}
		return action.execute();
	}

}
