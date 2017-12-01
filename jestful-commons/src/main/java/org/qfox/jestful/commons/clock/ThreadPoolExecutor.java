package org.qfox.jestful.commons.clock;

import java.util.concurrent.ExecutorService;

/**
 * Created by Payne on 2017/12/2.
 */
public class ThreadPoolExecutor implements Executor {
    private final ExecutorService service;

    public ThreadPoolExecutor(ExecutorService service) {
        if (service == null) throw new NullPointerException();
        this.service = service;
    }

    @Override
    public void execute(final Execution execution) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                execution.execute();
            }
        });
    }

    @Override
    public void destroy() {
        service.shutdown();
    }
}
