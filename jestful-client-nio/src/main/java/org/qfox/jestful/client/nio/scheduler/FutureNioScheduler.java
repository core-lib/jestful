package org.qfox.jestful.client.nio.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.ActionFuture;
import org.qfox.jestful.client.scheduler.FutureScheduler;
import org.qfox.jestful.core.Action;

/**
 * Created by payne on 2017/3/26.
 */
public class FutureNioScheduler extends FutureScheduler implements NioScheduler {

    @Override
    public Object doMotivateSchedule(Client client, Action action) {
        try {
            ActionFuture future = new ActionFuture(action);
            action.getResult().setValue(future);
            action.execute();
            return future;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doCallbackSchedule(Client client, Action action) {
        ActionFuture future = (ActionFuture) action.getResult().getValue();
        future.done();
    }

}
