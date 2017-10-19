package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Promise;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by yangchangpei on 17/10/18.
 */
public class RetryController implements Actor {
    private RetryCondition retryCondition;
    private int maxTimes;

    public RetryController() {
    }

    public RetryController(RetryCondition retryCondition, int maxTimes) {
        this.retryCondition = retryCondition;
        this.maxTimes = maxTimes;
    }

    @Override
    public Object react(Action action) throws Exception {
        if (retryCondition == null || maxTimes <= 0) return action.execute();
        Promise promise = (Promise) action.execute();
        return new RetryPromise(action, promise, retryCondition, maxTimes);
    }

    public RetryCondition getRetryCondition() {
        return retryCondition;
    }

    public void setRetryCondition(RetryCondition retryCondition) {
        this.retryCondition = retryCondition;
    }

    public int getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }
}
