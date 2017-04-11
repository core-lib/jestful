package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Action;

import java.net.SocketAddress;

/**
 * Created by yangchangpei on 17/4/11.
 */
public interface NioProcessor extends Runnable {

    void process(SocketAddress address, Action action);

    int tasks();

}
