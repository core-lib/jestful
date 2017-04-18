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
public class UILambdaNioScheduler extends UILambdaScheduler implements NioScheduler {
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
                Parameter success = parameters.first(UIOnSuccess.class);
                UIOnSuccess onSuccess = success != null && success.getValue() != null ? (UIOnSuccess) success.getValue() : UIOnSuccess.DEFAULT;
                Parameter fail = parameters.first(UIOnFail.class);
                UIOnFail onFail = fail != null && fail.getValue() != null ? (UIOnFail) fail.getValue() : UIOnFail.DEFAULT;
                Parameter completed = parameters.first(UIOnCompleted.class);
                UIOnCompleted onCompleted = completed != null && completed.getValue() != null ? (UIOnCompleted) completed.getValue() : UIOnCompleted.DEFAULT;

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
