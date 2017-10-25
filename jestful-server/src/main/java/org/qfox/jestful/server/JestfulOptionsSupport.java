package org.qfox.jestful.server;

import org.qfox.jestful.core.Mapping;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月2日 上午11:23:12
 * @since 1.0.0
 */
public class JestfulOptionsSupport implements Filter {
    private MappingRegistry mappingRegistry;

    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        String name = config.getInitParameter("mappingRegistry");
        name = name == null || name.length() == 0 ? "jestfulMappingRegistry" : name;
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
                Set<String> methods = new HashSet<String>();
                StringBuilder allow = new StringBuilder();
                for (Mapping mapping : mappings) {
                    String method = mapping.getRestful().getMethod();
                    if (methods.add(method.toUpperCase())) {
                        allow.append(allow.length() == 0 ? "" : ", ").append(method);
                    }
                }
                httpServletResponse.setHeader("Allow", allow.toString());
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
