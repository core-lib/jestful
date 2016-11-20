package org.qfox.jestful.server;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchangpei on 16/11/20.
 */
public class JestfulServletSession extends JestfulServletSessionWrapper implements SessionSubject {
    private final List<SessionListener> listeners = new ArrayList<SessionListener>();

    public JestfulServletSession(HttpSession session) {
        super(session);
    }

    @Override
    public void setAttribute(String name, Object value) {
        super.setAttribute(name, value);
        fire();
    }

    @Override
    public void putValue(String name, Object value) {
        super.putValue(name, value);
        fire();
    }

    @Override
    public void removeAttribute(String name) {
        super.removeAttribute(name);
        fire();
    }

    @Override
    public void removeValue(String name) {
        super.removeValue(name);
        fire();
    }

    public void fire() {
        for (SessionListener listener : listeners) {
            listener.onSessionChanged(this);
        }
    }

    public HttpSession getSession() {
        return this;
    }

    public void addSessionListener(SessionListener listener) {
        if (listener == null) {
            throw new NullPointerException("can not add null session listener");
        }
        this.listeners.add(listener);
    }

    public void removeSessionListener(SessionListener listener) {
        this.listeners.remove(listener);
    }

}
