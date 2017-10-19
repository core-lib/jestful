package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.core.Action;

class UICallbackTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final UICallback<Object> callback;
    private Exception exception;

    UICallbackTask(Action action, UICallback<Object> callback) {
        super();
        this.action = action;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object... parameters) {
        try {
            Promise promise = (Promise) action.execute();
            return promise.get();
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            if (exception == null) {
                callback.onSuccess(result);
            } else {
                callback.onFail(exception);
            }
        } finally {
            callback.onCompleted(exception == null, result, exception);
        }
    }

}