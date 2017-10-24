package org.qfox.jestful.android;

import org.qfox.jestful.client.scheduler.Callback;

class UICallbackDecorator<R> implements Callback<R> {
    private final UICallback<R> callback;

    UICallbackDecorator(UICallback<R> callback) {
        this.callback = callback;
    }

    @Override
    public void onCompleted(boolean success, R result, Exception exception) {
        callback.onCompleted(success, result, exception);
    }

    @Override
    public void onSuccess(R result) {
        callback.onSuccess(result);
    }

    @Override
    public void onFail(Exception exception) {
        callback.onFail(exception);
    }
}
