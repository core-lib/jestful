package org.qfox.jestful.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.JestfulHeadRequest;
import javax.servlet.http.JestfulHeadResponse;

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
 * @date 2016年6月1日 下午9:58:37
 *
 * @since 1.0.0
 */
public class JestfulHeadSupport implements Filter {

	public void init(FilterConfig config) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			if (httpServletRequest.getMethod().equalsIgnoreCase("HEAD")) {
				JestfulHeadRequest jestfulHeadRequest = new JestfulHeadRequest(httpServletRequest);
				JestfulHeadResponse jestfulHeadResponse = new JestfulHeadResponse(httpServletResponse);
				chain.doFilter(jestfulHeadRequest, jestfulHeadResponse);
				jestfulHeadResponse.finish();
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {

	}

}
