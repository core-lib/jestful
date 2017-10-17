package org.qfox.jestful.client;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;

/**
 * Created by yangchangpei on 17/10/17.
 */
public interface Promise {

    Object get() throws Exception;

    void get(Callback<Object> callback);

    void get(OnCompleted<Object> onCompleted);

    void get(OnSuccess<Object> onSuccess);

    void get(OnFail onFail);

}
