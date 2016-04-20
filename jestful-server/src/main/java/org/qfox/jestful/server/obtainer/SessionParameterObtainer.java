package org.qfox.jestful.server.obtainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
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
 * @date 2016年4月20日 下午6:42:30
 *
 * @since 1.0.0
 */
public class SessionParameterObtainer implements Obtainer {

	public Object obtain(Action action, Parameter parameter) {
		Request request = action.getRequest();
		if (request instanceof HttpServletRequest == false) {
			return null;
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession(true);
		if (parameter.getKlass().isInstance(session)) {
			return session;
		}
		return null;
	}

}
