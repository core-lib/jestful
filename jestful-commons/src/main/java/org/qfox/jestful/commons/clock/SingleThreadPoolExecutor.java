package org.qfox.jestful.commons.clock;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Payne on 2017/12/2.
 */
public class SingleThreadPoolExecutor extends FixedThreadPoolExecutor {

    public SingleThreadPoolExecutor() {
        super(1);
    }

    public SingleThreadPoolExecutor(ThreadFactory threadFactory) {
        super(1, threadFactory);
    }

}
