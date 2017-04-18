package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.core.Action;

class UICallbackTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final UICallback<Object> callback;
    private Throwable throwable;

    UICallbackTask(Action action, UICallback<Object> callback) {
        super();
        this.action = action;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object... parameters) {
        try {
            return action.execute();
        } catch (Throwable e) {
            throwable = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            if (throwable == null) {
                callback.onSuccess(result);
            } else {
                callback.onFail(throwable);
            }
        } finally {
            callback.onCompleted(throwable == null, result, throwable);
        }
    }

}