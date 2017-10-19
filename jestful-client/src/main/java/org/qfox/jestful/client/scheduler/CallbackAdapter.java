package org.qfox.jestful.client.scheduler;

/**
 * Created by yangchangpei on 17/10/18.
 */
public abstract class CallbackAdapter<R> implements Callback<R> {

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
