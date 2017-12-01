package org.qfox.jestful.commons.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Payne on 2017/12/2.
 */
public class CachedThreadPoolExecutor extends ThreadPoolExecutor {

    public CachedThreadPoolExecutor() {
        super(Executors.newCachedThreadPool());
    }

    public CachedThreadPoolExecutor(ThreadFactory threadFactory) {
        super(Executors.newCachedThreadPool(threadFactory));
    }

}
