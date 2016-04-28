package org.qfox.jestful.client;

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
 * @date 2016年4月28日 下午8:12:17
 *
 * @since 1.0.0
 */
public class CookieParameterProcessor implements Actor {

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String charset = action.getCharset();
		Parameter[] parameters = action.getParameters();
		flag: for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.HEADER) {
				continue;
			}
		}
		return action.execute();
	}

}
