package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/3/25.
 */
public interface NioEventListener {

    void onConnected(Action action) throws Exception;

    void onRequested(Action action) throws Exception;

    void onCompleted(Action action) throws Exception;

    void onException(Action action, Exception exception);

}
