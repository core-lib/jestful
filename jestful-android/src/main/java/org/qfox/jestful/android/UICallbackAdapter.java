package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/10/19.
 */
public abstract class UICallbackAdapter<R> implements UICallback<R> {

    @Override
    public void onCompleted(boolean success, R result, Exception exception) {

    }

    @Override
    public void onSuccess(R result) {

    }

    @Override
    public void onFail(Exception exception) {

    }
}
