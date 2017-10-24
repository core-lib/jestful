package org.qfox.jestful.android;

import org.qfox.jestful.client.scheduler.Callback;

class UILambdaDecorator<R> implements Callback<R> {
    private final UIOnSuccess<R> onSuccess;
    private final UIOnFail onFail;
    private final UIOnCompleted<R> onCompleted;

    UILambdaDecorator(UIOnSuccess<R> onSuccess, UIOnFail onFail, UIOnCompleted<R> onCompleted) {
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.onCompleted = onCompleted;
    }

    @Override
    public void onCompleted(boolean success, R result, Exception exception) {
        onCompleted.call(success, result, exception);
    }

    @Override
    public void onSuccess(R result) {
        onSuccess.call(result);
    }

    @Override
    public void onFail(Exception exception) {
        onFail.call(exception);
    }
}
