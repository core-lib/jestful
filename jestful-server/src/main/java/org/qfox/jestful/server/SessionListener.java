package org.qfox.jestful.server;

/**
 * Created by yangchangpei on 16/11/20.
 */
public interface SessionListener {

    void onSessionChanged(SessionSubject subject);

}
