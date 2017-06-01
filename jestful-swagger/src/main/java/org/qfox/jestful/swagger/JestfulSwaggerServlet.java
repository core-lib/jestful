package org.qfox.jestful.swagger;

import org.qfox.jestful.commons.IOKit;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by yangchangpei on 17/6/1.
 */
public class JestfulSwaggerServlet implements Servlet {
    private ServletConfig servletConfig;
    private ServletContext servletContext;
    private JestfulSwagger jestfulSwaggerDefinition;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        this.servletContext = config.getServletContext();
        this.jestfulSwaggerDefinition = new JestfulSwagger(servletContext);
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestURI = request.getRequestURI();
        String name = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        if ("swagger.json".equals(name)) {
            return;
        }
        URL resource = servletContext.getClassLoader().getResource("swagger-ui/" + name);
        if (resource == null) {
            String context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
            String servlet = request.getServletPath();
            response.sendRedirect(context + servlet + "/index.html");
        } else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = resource.openStream();
                out = response.getOutputStream();
                IOKit.transfer(in, out);
            } catch (IOException e) {
                throw e;
            } finally {
                IOKit.close(in);
                IOKit.close(out);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Jestful Swagger Integration";
    }

    @Override
    public void destroy() {

    }

}
