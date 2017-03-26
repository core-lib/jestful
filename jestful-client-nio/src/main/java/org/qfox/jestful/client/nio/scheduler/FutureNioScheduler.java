package org.qfox.jestful.client.nio.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.FutureScheduler;
import org.qfox.jestful.core.Action;

import java.util.concurrent.*;

/**
 * Created by payne on 2017/3/26.
 */
public class FutureNioScheduler extends FutureScheduler implements NioScheduler {

    @Override
    public Object doMotivateSchedule(Client client, Action action) {
        try {
            NioFuture future = new NioFuture(action);
            action.getResult().setValue(future);
            action.execute();
            return future;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doCallbackSchedule(Client client, Action action) {
        NioFuture future = (NioFuture) action.getResult().getValue();
        future.done();
    }

    private static class NioFuture implements Future<Object> {
        private final Action action;
        private boolean done;
        private boolean canceled;

        NioFuture(Action action) {
            this.action = action;
        }

        @Override
        public synchronized boolean cancel(boolean mayInterruptIfRunning) {
            if (isDone()) {
                return false;
            }
            canceled = true;
            this.notifyAll();
            return true;
        }

        @Override
        public synchronized boolean isCancelled() {
            return canceled;
        }

        synchronized boolean done() {
            if (isDone()) {
                return false;
            }
            done = true;
            this.notifyAll();
            return true;
        }

        @Override
        public synchronized boolean isDone() {
            return done;
        }

        @Override
        public synchronized Object get() throws InterruptedException, ExecutionException {
            if (isCancelled()) {
                throw new CancellationException();
            } else if (isDone()) {
                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                if (exception != null) throw new ExecutionException(exception);
                else return body;
            } else {
                this.wait();
                return get();
            }
        }

        @Override
        public synchronized Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (isCancelled()) {
                throw new CancellationException();
            } else if (isDone()) {
                Object body = action.getResult().getBody().getValue();
                Exception exception = action.getResult().getException();
                if (exception != null) throw new ExecutionException(exception);
                else return body;
            } else {
                this.wait(unit.toMillis(timeout));
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
