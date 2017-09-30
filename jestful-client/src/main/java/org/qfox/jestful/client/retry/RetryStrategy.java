package org.qfox.jestful.client.retry;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/9/30.
 */
public interface RetryStrategy {

    boolean matches(Action action);

    void reset(Action action);

    RetryStrategy ON_EXCEPTION = new RetryStrategy() {
        @Override
        public boolean matches(Action action) {
            return action.getResult().getException() != null;
        }

        @Override
        public void reset(Action action) {
            action.getResult().setException(null);
        }
    };

    RetryStrategy ON_STATUS_EXCEPTION = new RetryStrategy() {
        @Override
        public boolean matches(Action action) {
            return action.getResult().getException() instanceof StatusException;
        }

        @Override
        public void reset(Action action) {
            action.getResult().setException(null);
        }
    };

}
