package org.qfox.jestful.client.scheduler;

/**
 * Created by yangchangpei on 17/3/31.
 */
public interface OnFail extends OnLambda {

    OnFail DEFAULT = new OnFail() {

        @Override
        public void call(Exception exception) {
            throw exception instanceof RuntimeException ? (RuntimeException) exception : new RuntimeException(exception);
        }

    };

    void call(Exception exception);

}
