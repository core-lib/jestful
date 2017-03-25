package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by yangchangpei on 17/3/25.
 */
public interface NioCallback {

    void onConnected(Action action) throws IOException;

    void onRequested(Action action) throws IOException;

    void onResponsed(Action action) throws IOException;

    void onException(Exception exception);

}
