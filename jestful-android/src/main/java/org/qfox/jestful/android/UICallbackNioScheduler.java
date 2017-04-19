package org.qfox.jestful.android;

import android.os.Handler;
import android.os.Looper;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.scheduler.NioScheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;

/**
 * Created by payne on 2017/3/26.
 */
public class UICallbackNioScheduler extends UICallbackScheduler implements NioScheduler {
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
    public void doCallbackSchedule(Client client, final Action action) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Parameters parameters = action.getParameters();
                Parameter parameter = parameters.unique(UICallback.class);
                @SuppressWarnings("unchecked")
                UICallback<Object> callback = parameter.getValue() != null ? (UICallback<Object>) parameter.getValue() : UICallback.NULL;

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