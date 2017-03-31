package org.qfox.jestful.client.nio.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackScheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;

/**
 * Created by payne on 2017/3/26.
 */
public class CallbackNioScheduler extends CallbackScheduler implements NioScheduler {

    @Override
    public Object doMotivateSchedule(final Client client, final Action action) {
        try {
            action.execute();
        } catch (Exception e) {
            action.getResult().setException(e);
            doCallbackSchedule(client, action);
        }
        return null;
    }

    @Override
    public void doCallbackSchedule(final Client client, final Action action) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Parameters parameters = action.getParameters();
                Parameter parameter = parameters.unique(Callback.class);
                Callback callback = parameter.getValue() != null ? (Callback) parameter.getValue() : Callback.NULL;

                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                try {
                    if (exception == null) {
                        callback.onSuccess(body);
                    } else {
                        throw exception;
                    }
                } catch (Exception e) {
                    exception = e;
                    callback.onFail(e);
                } finally {
                    callback.onCompleted(exception == null, body, exception);
                }
            }
        });
    }

}
