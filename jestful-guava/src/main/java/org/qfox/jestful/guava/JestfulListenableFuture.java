package org.qfox.jestful.guava;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;

public class JestfulListenableFuture extends AbstractFuture<Object> implements ListenableFuture<Object> {

    @Override
    protected boolean set(Object value) {
        return super.set(value);
    }

    @Override
    protected boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }

}