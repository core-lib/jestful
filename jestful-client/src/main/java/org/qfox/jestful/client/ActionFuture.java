package org.qfox.jestful.client;

import org.qfox.jestful.core.Action;

import java.util.concurrent.*;

public class ActionFuture implements Future<Object> {
    private final Object lock = new Object();
    private final Action action;
    private boolean done;
    private boolean canceled;

    public ActionFuture(Action action) {
        this.action = action;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (isCancelled()) {
                return true;
            }
            if (isDone()) {
                return false;
            }
            canceled = true;
            lock.notifyAll();
            return true;
        }
    }

    @Override
    public boolean isCancelled() {
        synchronized (lock) {
            return canceled;
        }
    }

    public boolean done() {
        synchronized (lock) {
            if (isDone()) {
                return true;
            }
            if (isCancelled()) {
                return false;
            }
            done = true;
            lock.notifyAll();
            return true;
        }
    }

    @Override
    public boolean isDone() {
        synchronized (lock) {
            return done;
        }
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (isCancelled()) {
                throw new CancellationException();
            } else if (isDone()) {
                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                if (exception != null) throw new ExecutionException(exception);
                else return body;
            } else {
                lock.wait();
                return get();
            }
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (isCancelled()) {
                throw new CancellationException();
            } else if (isDone()) {
                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                if (exception != null) throw new ExecutionException(exception);
                else return body;
            } else {
                lock.wait(unit.toMillis(timeout));
            }

            if (isCancelled()) {
                throw new CancellationException();
            } else if (isDone()) {
                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                if (exception != null) throw new ExecutionException(exception);
                else return body;
            } else {
                throw new TimeoutException();
            }
        }
    }
}