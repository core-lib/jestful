package org.qfox.jestful.client.aio.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.FutureScheduler;
import org.qfox.jestful.core.Action;

import java.util.concurrent.*;

/**
 * Created by payne on 2017/3/26.
 */
public class FutureAioScheduler extends FutureScheduler implements AioScheduler {

    @Override
    public Object doMotivateSchedule(Client client, Action action) {
        try {
            AioFuture future = new AioFuture(action);
            action.getResult().setValue(future);
            action.execute();
            return future;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doCallbackSchedule(Client client, Action action) {
        AioFuture future = (AioFuture) action.getResult().getValue();
        future.done();
    }

    private static class AioFuture implements Future<Object> {
        private final Object lock = new Object();
        private final Action action;
        private boolean done;
        private boolean canceled;

        AioFuture(Action action) {
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

        boolean done() {
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

}
