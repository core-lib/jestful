package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Promise;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class AuthManager implements Actor {

    @Override
    public Object react(Action action) throws Exception {
        Promise promise = (Promise) action.execute();
        return new AuthPromise(promise);
    }

}
