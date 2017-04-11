package org.qfox.jestful.client.nio.balancer;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioProcessor;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;
import java.util.Random;

/**
 * Created by yangchangpei on 17/4/11.
 */
public class RandomNioBalancer implements NioBalancer {
    private final Random random;

    public RandomNioBalancer() {
        this.random = new Random();
    }

    @Override
    public void dispatch(SocketAddress address, Action action, NioClient client, NioProcessor[] processors) {
        int index = random.nextInt(processors.length);
        NioProcessor processor = processors[index];
        processor.process(address, action);
    }

}
