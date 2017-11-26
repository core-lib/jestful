package org.qfox.jestful.client.nio.balancer;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioProcessor;
import org.qfox.jestful.core.Action;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/4/11.
 */
public class LoopedNioBalancer implements NioBalancer {
    private final ConcurrentMap<NioProcessor[], AtomicInteger> map = new ConcurrentHashMap<NioProcessor[], AtomicInteger>();

    @Override
    public void dispatch(Action action, NioClient client, NioProcessor[] processors) {
        if (processors == null || processors.length == 0) throw new IllegalArgumentException("processors is null or empty");
        AtomicInteger index = map.get(processors);
        if (index == null) {
            AtomicInteger old = map.putIfAbsent(processors, index = new AtomicInteger(-1));
            if (old != null) index = old;
        }
        int i = index.incrementAndGet();
        while (i >= processors.length) {
            index.compareAndSet(i, 0);
            i = index.get();
        }
        processors[i].process(action);
    }

}
