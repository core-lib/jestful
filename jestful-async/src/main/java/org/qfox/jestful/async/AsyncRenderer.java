package org.qfox.jestful.async;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.server.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/5/5.
 */
public abstract class AsyncRenderer implements Renderer, Initialable, Destroyable {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ExecutorService executor;

    @Override
    public void initialize(BeanContainer beanContainer) {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void destroy() {
        if (executor != null) executor.shutdown();
    }

}
