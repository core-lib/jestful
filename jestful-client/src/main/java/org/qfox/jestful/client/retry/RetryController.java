package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Promise;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by yangchangpei on 17/10/18.
 */
public class RetryController implements Actor {
    private RetryCondition retryCondition;
    private int maxCount;

    public RetryController() {
    }

    public RetryController(RetryCondition retryCondition, int maxCount) {
        this.retryCondition = retryCondition;
        this.maxCount = maxCount;
    }

    @Override
    public Object react(Action action) throws Exception {
        if (retryCondition == null || maxCount <= 0) return action.execute();
        Promise promise = (Promise) action.execute();
        return new RetryPromise(action, promise, retryCondition, maxCount);
    }

    public RetryCondition getRetryCondition() {
        return retryCondition;
    }

    public void setRetryCondition(RetryCondition retryCondition) {
        this.retryCondition = retryCondition;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
