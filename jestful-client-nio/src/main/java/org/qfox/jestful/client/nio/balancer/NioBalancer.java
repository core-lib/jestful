package org.qfox.jestful.client.nio.balancer;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioProcessor;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/4/11.
 */
public interface NioBalancer {

    void dispatch(Action action, NioClient client, NioProcessor[] processors);

}
