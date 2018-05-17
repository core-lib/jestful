package org.qfox.jestful.guava;

import com.google.common.util.concurrent.ExecutionList;
import com.google.common.util.concurrent.ListenableFuture;
import org.qfox.jestful.client.ActionFuture;
import org.qfox.jestful.core.Action;

import java.util.concurrent.Executor;

public class ActionListenableFuture extends ActionFuture implements ListenableFuture<Object> {
    private final ExecutionList executionList = new ExecutionList();

    public ActionListenableFuture(Action action) {
        super(action);
    }

    @Override
    public void addListener(Runnable listener, Executor executor) {
        executionList.add(listener, executor);
    }

    @Override
    public boolean done() {
        executionList.execute();
        return super.done();
    }
}