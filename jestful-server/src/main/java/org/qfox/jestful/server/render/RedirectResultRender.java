package org.qfox.jestful.server.render;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Response;
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
 * @date 2016年4月26日 下午5:25:31
 *
 * @since 1.0.0
 */
public class RedirectResultRender implements Actor, Initialable {
	private String ctxpath = "";
	private String prefix = "redirect:";

	public void initialize(BeanContainer beanContainer) {
		ServletContext servletContext = beanContainer.get(ServletContext.class);
		this.ctxpath = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
	}

	public Object react(Action action) throws Exception {
		Object value = action.execute();
		Result result = action.getResult();
		if (result.isRendered()) {
			return value;
		}
		if (value instanceof String == false) {
			return value;
		}
		Response response = action.getResponse();
		if (response instanceof HttpServletResponse == false) {
			return value;
		}
		String text = (String) value;
		if (text.startsWith(prefix)) {
			String path = text.substring(prefix.length());
			path = path.contains("://") ? path : ctxpath + path;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.sendRedirect(path);
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
