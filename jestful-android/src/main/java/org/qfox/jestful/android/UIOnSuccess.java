package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface UIOnSuccess<R> extends UIOnLambda {

    UIOnSuccess<Object> DEFAULT = new UIOnSuccess<Object>() {

        @Override
        public void call(Object result) {

        }

    };

    void call(R result);

}
