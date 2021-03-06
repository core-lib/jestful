package org.qfox.jestful.client;

import org.qfox.jestful.client.scheduler.Callback;

/**
 * Created by yangchangpei on 17/10/17.
 */
public interface Promise {

    Object acquire() throws Exception;

    void accept(Callback<Object> callback);

    void cancel();

    Client client();

}
