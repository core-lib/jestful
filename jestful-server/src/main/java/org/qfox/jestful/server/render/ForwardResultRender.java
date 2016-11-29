package org.qfox.jestful.server.render;

import org.qfox.jestful.core.*;
import org.qfox.jestful.server.exception.UnknownContextException;
import org.qfox.jestful.server.exception.UnsupportedForwardException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
 * @date 2016年4月26日 下午5:24:37
 * @since 1.0.0
 */
public class ForwardResultRender implements Actor, Initialable {
    private ServletContext servletContext;
    private String ctxpath = "";
    private String prefix = "forward:";

    public void initialize(BeanContainer beanContainer) {
        servletContext = beanContainer.get(ServletContext.class);
        this.ctxpath = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
        this.ctxpath = this.ctxpath.startsWith("/") ? this.ctxpath : "/" + this.ctxpath;
    }

    public Object react(Action action) throws Exception {
        Object value = action.execute();
        Result result = action.getResult();
        if (result.isRendered()) {
            return value;
        }
        if (value instanceof String == false) {
            return value;
        }
        String string = (String) value;
        if (string.startsWith(prefix)) {
            String expression = string.substring(prefix.length());
            int index = expression.indexOf(':');
            String ctx = null;
            String path = null;
            if (index < 0) {
                ctx = ctxpath;
                path = expression;
            } else {
                ctx = expression.substring(0, index);
                path = expression.substring(index + 1);
            }
            ServletRequest servletRequest = (ServletRequest) action.getExtra().get(ServletRequest.class);
            ServletResponse servletResponse = (ServletResponse) action.getExtra().get(ServletResponse.class);
            ServletContext context = this.servletContext.getContext(ctx);
            if (context == null) {
                throw new UnknownContextException(ctx);
            }
            RequestDispatcher dispatcher = context.getRequestDispatcher(path);
            if (dispatcher == null) {
                throw new UnsupportedForwardException(context);
            }
            switch (servletRequest.getDispatcherType()) {
                case INCLUDE:
                    dispatcher.include(servletRequest, servletResponse);
                    break;
                default:
                    servletResponse.setContentType("text/html");
                    dispatcher.forward(servletRequest, servletResponse);
                    break;
            }
            result.setRendered(true);
        }
        return value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
