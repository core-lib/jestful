package org.qfox.jestful.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/6/1.
 */
public class JestfulSwaggerServlet implements Servlet {
    private ServletConfig servletConfig;
    private ServletContext servletContext;
    private ClassLoader classLoader;
    private ApplicationContext applicationContext;
    private SpringSwaggerScanner springSwaggerScanner;
    private Swagger swagger;
    private ObjectMapper swaggerJsonMapper;
    private ObjectMapper swaggerYamlMapper;
    private Map<String, URL> cache = new LinkedHashMap<>();
    private MappingRegistry mappingRegistry;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        this.servletContext = config.getServletContext();
        this.classLoader = servletContext.getClassLoader();
        this.applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.springSwaggerScanner = new SpringSwaggerScanner();
        this.swaggerJsonMapper = new SwaggerJsonMapper();
        this.swaggerYamlMapper = new SwaggerYamlMapper();
        String name = config.getInitParameter("mappingRegistry");
        name = name == null || name.length() == 0 ? "jestfulMappingRegistry" : name;
        mappingRegistry = applicationContext.getBean(name, MappingRegistry.class);
        this.swagger = springSwaggerScanner.scan(applicationContext, mappingRegistry);
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
            swaggerJsonMapper.writeValue(response.getOutputStream(), swagger);
            return;
        }
        if ("swagger.yaml".equals(name)) {
            swaggerYamlMapper.writeValue(response.getOutputStream(), swagger);
            return;
        }
        URL resource = cache.get(name);
        if (resource == null && (resource = classLoader.getResource("swagger-ui/" + name)) != null) {
            cache.put(name, resource);
        }
        if (resource == null) {
            String context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
            String servlet = request.getServletPath();
            response.sendRedirect(context + servlet + "/index.html");
        } else {
            InputStream in = null;
            try {
                in = resource.openStream();
                IOKit.transfer(in, response.getOutputStream());
            } catch (IOException e) {
                throw e;
            } finally {
                IOKit.close(in);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Controller Swagger Integration";
    }

    @Override
    public void destroy() {
        cache.clear();
    }

}
