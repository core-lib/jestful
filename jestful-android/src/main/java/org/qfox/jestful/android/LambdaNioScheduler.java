package org.qfox.jestful.android;

import android.os.Handler;
import android.os.Looper;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.scheduler.NioScheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;

/**
 * Created by yangchangpei on 17/3/31.
 */
public class LambdaNioScheduler extends LambdaScheduler implements NioScheduler {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public Object doMotivateSchedule(Client client, Action action) {
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
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Parameters parameters = action.getParameters();
                Parameter success = parameters.first(OnSuccess.class);
                OnSuccess onSuccess = success != null && success.getValue() != null ? (OnSuccess) success.getValue() : OnSuccess.DEFAULT;
                Parameter fail = parameters.first(OnFail.class);
                OnFail onFail = fail != null && fail.getValue() != null ? (OnFail) fail.getValue() : OnFail.DEFAULT;
                Parameter completed = parameters.first(OnCompleted.class);
                OnCompleted onCompleted = completed != null && completed.getValue() != null ? (OnCompleted) completed.getValue() : OnCompleted.DEFAULT;

                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                try {
                    if (exception == null) {
                        onSuccess.call(body);
                    } else {
                        throw exception;
                    }
                } catch (Exception e) {
                    exception = e;
                    onFail.call(e);
                } finally {
                    onCompleted.call(exception == null, body, exception);
                }
            }
        });
    }
}
