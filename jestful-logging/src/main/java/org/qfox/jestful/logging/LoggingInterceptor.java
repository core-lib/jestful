package org.qfox.jestful.logging;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BackPlugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

/**
 * 日志拦截器
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:02
 */
public class LoggingInterceptor implements BackPlugin {

    @Override
    public Object react(Action action) throws Exception {
        Request oldRequest = action.getRequest();
        Response oldResponse = action.getResponse();
        action.setRequest(new LoggingRequest(oldRequest));
        action.setResponse(new LoggingResponse(oldResponse));
        return action.execute();
    }
}
