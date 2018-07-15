package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.server.SessionListener;
import org.qfox.jestful.server.SessionSubject;
import org.qfox.jestful.server.annotation.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class SessionResolver implements Resolver {
    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.between(Session.POSITION) && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        Request request = action.getRequest();
        if (!(request instanceof HttpServletRequest)) return;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        if (session == null) return;

        String key = parameter.getName();
        Object value = session.getAttribute(key);
        parameter.setValue(value);

        if (session instanceof SessionSubject) ((SessionSubject) session).addSessionListener(new Updater(parameter));
    }

    private static class Updater implements SessionListener {
        private final Parameter parameter;

        Updater(Parameter parameter) {
            this.parameter = parameter;
        }

        public void onSessionChanged(SessionSubject subject) {
            HttpSession session = subject.getSession();
            String key = parameter.getName();
            Object value = session.getAttribute(key);
            parameter.setValue(value);
        }

    }
}
