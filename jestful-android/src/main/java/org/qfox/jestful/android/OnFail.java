package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface OnFail extends OnLambda {

    OnFail DEFAULT = new OnFail() {

        @Override
        public void call(Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    };

    void call(Throwable throwable);

}
