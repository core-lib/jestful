package org.qfox.jestful.android;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface UIOnFail extends UIOnLambda {

    UIOnFail DEFAULT = new UIOnFail() {

        @Override
        public void call(Exception exception) {
            throw exception instanceof RuntimeException ? (RuntimeException) exception : new RuntimeException(exception);
        }

    };

    void call(Exception exception);

}
