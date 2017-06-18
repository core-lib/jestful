package org.qfox.jestful.server;

import org.qfox.jestful.core.exception.StatusException;
import org.qfox.jestful.server.exception.NotFoundStatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Description: 框架过滤器, 该过滤器只拦截和处理已经映射的路径, 如果不路径不存在则交给下一个过滤器处理, 本身不报404错误
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月7日 上午11:37:48
 * @since 1.0.0
 */
public class JestfulFilterSupport extends JestfulWebSupport implements Filter {

    public void init(FilterConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        Map<String, String> configuration = new LinkedHashMap<String, String>();
        Enumeration<String> enumeration = config.getInitParameterNames();
        while (enumeration != null && enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = config.getInitParameter(name);
            configuration.put(name, value);
        }
        super.init(servletContext, configuration);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            handle(httpServletRequest, httpServletResponse);
        } catch (NotFoundStatusException e) {
            chain.doFilter(request, response);
        } catch (StatusException e) {
            logger.error(e.getMessage(), e);
            httpServletRequest.setAttribute("exception", e);
            httpServletResponse.sendError(e.getStatus(), e.getMessage());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            httpServletRequest.setAttribute("exception", e);
            httpServletResponse.sendError(500, e.getMessage());
        }
    }

    public void destroy() {
        super.destroy();
    }

}
