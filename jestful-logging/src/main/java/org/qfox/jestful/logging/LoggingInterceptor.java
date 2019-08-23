package org.qfox.jestful.logging;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BackPlugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:02
 */
public class LoggingInterceptor implements BackPlugin {
    private final ILogger logger;

    public LoggingInterceptor() {
        this(new Slf4jLogger(LoggerFactory.getLogger(LoggingInterceptor.class)));
    }

    public LoggingInterceptor(ILogger logger) {
        this.logger = logger;
    }

    @Override
    public Object react(Action action) throws Exception {
        Request oldRequest = action.getRequest();
        Response oldResponse = action.getResponse();
        action.setRequest(new LoggingRequest(oldRequest, logger));
        action.setResponse(new LoggingResponse(oldResponse, logger));
        return action.execute();
    }

    public ILogger getLogger() {
        return logger;
    }

}
