package org.qfox.jestful.commons.clock;

/**
 * Created by Payne on 2017/12/1.
 */
public class CurrentThreadExecutor implements Executor {

    @Override
    public void execute(Execution execution) {
        execution.execute();
    }

}
