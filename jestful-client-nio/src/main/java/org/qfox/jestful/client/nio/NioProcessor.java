package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/4/11.
 */
public interface NioProcessor extends Runnable {

    void process(Action action);

    int tasks();

}
