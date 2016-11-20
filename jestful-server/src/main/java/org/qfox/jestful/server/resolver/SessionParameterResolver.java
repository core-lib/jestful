package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.server.SessionListener;
import org.qfox.jestful.server.SessionSubject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
 * @date 2016年7月11日 下午4:05:27
 * @since 1.0.0
 */
public class SessionParameterResolver implements Actor {

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        if (request instanceof HttpServletRequest == false) {
            return action.execute();
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            return action.execute();
        }
        List<Parameter> parameters = action.getParameters().all(Position.SESSION);
        for (Parameter parameter : parameters) {
            String key = parameter.getName();
            Object value = session.getAttribute(key);
            parameter.setValue(value);
        }
        if (session instanceof SessionSubject) {
            SessionSubject subject = (SessionSubject) session;
            subject.addSessionListener(new SessionParameterUpdater(parameters));
        }
        return action.execute();
    }

    public static class SessionParameterUpdater implements SessionListener {
        private final List<Parameter> parameters;

        public SessionParameterUpdater(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        public void onSessionChanged(SessionSubject subject) {
            HttpSession session = subject.getSession();
            for (Parameter parameter : parameters) {
                String key = parameter.getName();
                Object value = session.getAttribute(key);
                parameter.setValue(value);
            }
        }

    }

}
