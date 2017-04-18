package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface OnSuccess<R> extends OnLambda {

    OnSuccess<Object> DEFAULT = new OnSuccess<Object>() {

        @Override
        public void call(Object result) {

        }

    };

    void call(R result);

}
