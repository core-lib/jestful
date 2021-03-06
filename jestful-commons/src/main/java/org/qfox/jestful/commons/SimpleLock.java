package org.qfox.jestful.commons;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class SimpleLock implements Lock {

    @Override
    public synchronized void lockOne() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void lockOne(long timeout) {
        try {
            this.wait(timeout);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void lockOne(long timeout, int nanos) {
        try {
            this.wait(timeout, nanos);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public synchronized void openAny() {
        this.notify();
    }

    @Override
    public synchronized void openAll() {
        this.notifyAll();
    }

    @Override
    public synchronized void doWithLock(LockBlock block) {
        block.execute();
    }
}
