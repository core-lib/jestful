package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.PluginConfigException;
import org.qfox.jestful.server.exception.UnknownContextException;
import org.qfox.jestful.server.exception.UnsupportedForwardException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
 * @date 2016年4月26日 下午5:24:37
 * @since 1.0.0
 */
public class ForwardResultRenderer implements Plugin, Initialable {
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
    public void config(Map<String, String> arguments) throws PluginConfigException {
        this.prefix = arguments.containsKey("view-prefix") ? arguments.get("view-prefix") : "";
        this.suffix = arguments.containsKey("view-suffix") ? arguments.get("view-suffix") : "";
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
        String text = (String) value;
        if (text.startsWith(command)) {
            String expression = text.substring(command.length());
            int index = expression.indexOf(':');
            String ctx = null;
            String path = null;
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
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher(path.startsWith("/") ? path : prefix + path + suffix);
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
            result.setRendered(true);
        }
        return value;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
