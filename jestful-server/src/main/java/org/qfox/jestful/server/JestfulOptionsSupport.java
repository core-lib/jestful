package org.qfox.jestful.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.commons.collection.CaseInsensitiveSet;
import org.qfox.jestful.core.Mapping;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

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
 * @date 2016年6月2日 上午11:23:12
 *
 * @since 1.0.0
 */
public class JestfulOptionsSupport implements Filter {
	private MappingRegistry mappingRegistry;

	public void init(FilterConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		String name = config.getInitParameter("mappingRegistry");
		name = name == null || name.isEmpty() ? "jestfulMappingRegistry" : name;
		mappingRegistry = applicationContext.getBean(name, MappingRegistry.class);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
				String URI = httpServletRequest.getRequestURI();
				if (URI.equals("*") || URI.equals("/*")) {
					return;
				}
				Collection<Mapping> mappings = mappingRegistry.lookup(URI);
				Set<String> methods = new CaseInsensitiveSet<String>();
				String allow = "";
				for (Mapping mapping : mappings) {
					String method = mapping.getRestful().getMethod();
					if (methods.add(method)) {
						allow += (allow.isEmpty() ? "" : ", ") + method;
					}
				}
				httpServletResponse.setHeader("Allow", allow);
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
