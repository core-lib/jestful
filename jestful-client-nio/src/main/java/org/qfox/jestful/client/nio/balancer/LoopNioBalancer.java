package org.qfox.jestful.client.nio.balancer;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioProcessor;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;

/**
 * Created by yangchangpei on 17/4/11.
 */
public class LoopNioBalancer implements NioBalancer {

    @Override
    public void dispatch(SocketAddress address, Action action, NioClient client, NioProcessor[] processors) {

    }

}
