package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Promise;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.Map;

/**
 * Created by yangchangpei on 17/10/18.
 */
public class RetryPlugin implements Plugin {
    private RetryCondition retryCondition;
    private int maxTimes;

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {

    }

    @Override
    public Object react(Action action) throws Exception {
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
