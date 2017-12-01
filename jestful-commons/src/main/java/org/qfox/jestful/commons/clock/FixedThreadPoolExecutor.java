package org.qfox.jestful.commons.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Payne on 2017/12/2.
 */
public class FixedThreadPoolExecutor extends ThreadPoolExecutor {

    public FixedThreadPoolExecutor(int nThreads) {
        super(Executors.newFixedThreadPool(nThreads));
    }

    public FixedThreadPoolExecutor(int nThreads, ThreadFactory threadFactory) {
        super(Executors.newFixedThreadPool(nThreads, threadFactory));
    }

}
