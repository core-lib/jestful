package org.qfox.jestful.client.nio.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;

/**
 * Created by payne on 2017/3/26.
 */
public interface NioScheduler extends Scheduler {

    Object doMotivateSchedule(Client client, Action action);

    void doCallbackSchedule(Client client, Action action);

}
