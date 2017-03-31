package org.qfox.jestful.client.scheduler;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface OnCompleted<R> extends OnLambda {

    OnCompleted<Object> DEFAULT = new OnCompleted<Object>() {

        @Override
        public void call(boolean success, Object result, Throwable throwable) {

        }

    };

    void call(boolean success, R result, Throwable throwable);

}
