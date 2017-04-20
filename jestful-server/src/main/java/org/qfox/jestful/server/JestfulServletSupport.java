package org.qfox.jestful.server;

import org.qfox.jestful.core.exception.StatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

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
 * @date 2016年4月27日 下午3:03:35
 * @Deprecated @see {@link JestfulFilterSupport}
 * @since 1.0.0
 */
public class JestfulServletSupport extends JestfulWebSupport implements Servlet {
    private ServletConfig servletConfig;

    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
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

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            handle(httpServletRequest, httpServletResponse);
        } catch (StatusException e) {
            logger.error(e.getMessage(), e);
            httpServletRequest.setAttribute("exception", e);
            httpServletResponse.sendError(e.getStatus(), e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            httpServletRequest.setAttribute("exception", e);
            httpServletResponse.sendError(500, e.getMessage());
        }
    }

    public String getServletInfo() {
        return "jestful servlet support";
    }

    public void destroy() {
        super.destroy();
    }

}
