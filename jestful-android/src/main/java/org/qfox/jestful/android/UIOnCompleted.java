package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface UIOnCompleted<R> extends UIOnLambda {

    UIOnCompleted<Object> DEFAULT = new UIOnCompleted<Object>() {

        @Override
        public void call(boolean success, Object result, Throwable throwable) {

        }

    };

    void call(boolean success, R result, Throwable throwable);

}
