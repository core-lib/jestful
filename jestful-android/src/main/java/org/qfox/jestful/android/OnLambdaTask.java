package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.core.Action;

/**
 * Created by payne on 2017/4/18.
 * Version: 1.0
 */
class OnLambdaTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final OnSuccess onSuccess;
    private final OnFail onFail;
    private final OnCompleted onCompleted;
    private Throwable throwable;

    OnLambdaTask(Action action, OnSuccess onSuccess, OnFail onFail, OnCompleted onCompleted) {
        this.action = action;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.onCompleted = onCompleted;
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
                onSuccess.call(result);
            } else {
                onFail.call(throwable);
            }
        } finally {
            onCompleted.call(throwable == null, result, throwable);
        }
    }

}
