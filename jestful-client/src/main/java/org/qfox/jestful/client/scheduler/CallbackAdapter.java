package org.qfox.jestful.client.scheduler;

/**
 * Created by yangchangpei on 17/10/18.
 */
public abstract class CallbackAdapter<T> implements Callback<T> {

    @Override
    public void onCompleted(boolean success, T result, Exception exception) {

    }

    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onFail(Exception exception) {

    }

}
