package org.qfox.jestful.client.retry;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/18.
 */
public interface RetryCondition {

    boolean matches(Action action);

}
