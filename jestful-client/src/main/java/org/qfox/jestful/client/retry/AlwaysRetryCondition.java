package org.qfox.jestful.client.retry;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/19.
 */
public class AlwaysRetryCondition implements RetryCondition {

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return true;
    }

}
