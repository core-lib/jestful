package org.qfox.jestful.interception;

import org.qfox.jestful.core.Action;

/**
 * Created by Payne on 2017/5/5.
 */
class ActionInterceptor implements Interceptor {
    private final Action action;

    ActionInterceptor(Action action) {
        this.action = action;
    }

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        return action.execute();
    }

}
