package org.qfox.jestful.server;

import javax.servlet.http.HttpSession;

/**
 * Created by yangchangpei on 16/11/20.
 */
public interface SessionSubject {

    void fire();

    HttpSession getSession();

    void addSessionListener(SessionListener listener);

    void removeSessionListener(SessionListener listener);

}
