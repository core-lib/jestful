package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface UIOnFail extends UIOnLambda {

    UIOnFail DEFAULT = new UIOnFail() {

        @Override
        public void call(Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    };

    void call(Throwable throwable);

}
