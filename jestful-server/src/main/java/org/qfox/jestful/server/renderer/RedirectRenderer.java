package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Payne on 2017/4/27.
 */
public class RedirectRenderer implements Renderer, Initialable {
    private String context = "";
    private String command = "@redirect:";

    public void initialize(BeanContainer beanContainer) {
        ServletContext servletContext = beanContainer.get(ServletContext.class);
        this.context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
    }

    @Override
    public boolean supports(Action action, Object value) {
        return value instanceof String && ((String) value).startsWith(command);
    }

    @Override
    public void render(Action action, Object value, Request request, Response response) throws Exception {
        String text = (String) value;
        String path = text.substring(command.length());
        path = path.contains("://") ? path : context + path;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.sendRedirect(path);
    }
}
