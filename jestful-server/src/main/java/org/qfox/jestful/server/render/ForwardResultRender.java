package org.qfox.jestful.server.render;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Result;

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
 * @date 2016年4月26日 下午5:24:37
 *
 * @since 1.0.0
 */
public class ForwardResultRender implements Actor {
	private String prefix = "forward:";

	public Object react(Action action) throws Exception {
		Object value = action.execute();
		Result result = action.getResult();
		if (result.isRendered()) {
			return value;
		}
		if (value instanceof String == false) {
			return value;
		}
		String string = (String) value;
		if (string.startsWith(prefix)) {
			String path = string.substring(prefix.length());
			ServletRequest servletRequest = (ServletRequest) action.getExtra().get(ServletRequest.class);
			ServletResponse servletResponse = (ServletResponse) action.getExtra().get(ServletResponse.class);
			servletRequest.getRequestDispatcher(path).forward(servletRequest, servletResponse);
			result.setRendered(true);
		}
		return value;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
