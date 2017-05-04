package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.server.exception.UnknownContextException;
import org.qfox.jestful.server.exception.UnsupportedForwardException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * Created by Payne on 2017/4/27.
 */
public class ForwardRenderer implements Renderer, Initialable, Configurable {
    private ServletContext servletContext;
    private String context = "";
    private String command = "@forward:";

    private String prefix;
    private String suffix;

    public void initialize(BeanContainer beanContainer) {
        servletContext = beanContainer.get(ServletContext.class);
        this.context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
        this.context = this.context.startsWith("/") ? this.context : "/" + this.context;
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        this.prefix = arguments.containsKey("view-prefix") ? arguments.get("view-prefix") : "";
        this.suffix = arguments.containsKey("view-suffix") ? arguments.get("view-suffix") : "";
    }

    @Override
    public boolean supports(Action action, Object value) {
        return value instanceof String && ((String) value).startsWith(command);
    }

    @Override
    public void render(Action action, Object value, Request request, Response response) throws Exception {
        String text = (String) value;
        String expression = text.substring(command.length());
        int index = expression.indexOf(':');
        String ctx, path;
        if (index < 0) {
            ctx = context;
            path = expression;
        } else {
            ctx = expression.substring(0, index);
            path = expression.substring(index + 1);
        }
        ServletRequest servletRequest = (ServletRequest) action.getExtra().get(ServletRequest.class);
        ServletResponse servletResponse = (ServletResponse) action.getExtra().get(ServletResponse.class);
        ServletContext servletContext = this.servletContext.getContext(ctx);
        if (servletContext == null) {
            throw new UnknownContextException(ctx);
        }
        path = path.startsWith("/") ? path : prefix + path + suffix;

        if (servletRequest.isAsyncStarted()) {
            servletRequest.getAsyncContext().dispatch(servletContext, path);
            return;
        }

        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(path);
        if (dispatcher == null) {
            throw new UnsupportedForwardException(servletContext);
        }
        switch (servletRequest.getDispatcherType()) {
            case INCLUDE:
                dispatcher.include(servletRequest, servletResponse);
                break;
            default:
                dispatcher.forward(servletRequest, servletResponse);
                break;
        }
    }
}
