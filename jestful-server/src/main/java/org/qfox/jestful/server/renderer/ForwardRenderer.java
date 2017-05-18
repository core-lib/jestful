package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.server.View;
import org.qfox.jestful.server.exception.UnknownContextException;
import org.qfox.jestful.server.exception.UnsupportedForwardException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Payne on 2017/4/27.
 */
public class ForwardRenderer implements Renderer, Initialable, Destroyable, Configurable {
    private ServletContext servletContext;
    private String context = "";
    private String command = "@forward:";
    private Set<View> views = new LinkedHashSet<View>();

    private String prefix;
    private String suffix;

    public void initialize(BeanContainer beanContainer) {
        this.servletContext = beanContainer.get(ServletContext.class);
        this.context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
        this.context = this.context.startsWith("/") ? this.context : "/" + this.context;
        this.views.addAll(beanContainer.find(View.class).values());
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        this.prefix = arguments.containsKey("view-prefix") ? arguments.get("view-prefix") : "";
        this.suffix = arguments.containsKey("view-suffix") ? arguments.get("view-suffix") : "";
        for (View view : views) if (view instanceof Configurable) ((Configurable) view).config(arguments);
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

        // 多视图支持
        String extension = path.contains(".") ? path.substring(path.lastIndexOf('.')) : "";
        for (View view : views) {
            if (view.supports(action, extension)) {
                view.render(servletContext, path, action, request, response);
                return;
            }
        }

        // 否则直接盲目跳转
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

    @Override
    public void destroy() {
        for (View view : views) if (view instanceof Destroyable) ((Destroyable) view).destroy();
    }
}
