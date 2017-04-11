package org.qfox.jestful.client.nio.balancer;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioProcessor;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchangpei on 17/4/11.
 */
public class LoopedNioBalancer implements NioBalancer {
    private final Map<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();

    @Override
    public void dispatch(SocketAddress address, Action action, NioClient client, NioProcessor[] processors) {
        if (processors == null || processors.length == 0) throw new IllegalArgumentException("processors is empty");
        synchronized (processors) {
            Integer hashcode = processors.hashCode();
            Integer index = map.get(hashcode);
            if (index == null) map.put(hashcode, index = 0);
            else if (index == processors.length) index = 0;
            processors[index].process(address, action);
            map.put(hashcode, ++index);
        }
    }

}
