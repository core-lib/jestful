package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/3/25.
 */
public interface AioEventListener {

    void onConnected(Action action) throws Exception;

    void onRequested(Action action) throws Exception;

    void onResponsed(Action action) throws Exception;

    void onException(Action action, Exception exception);

}
