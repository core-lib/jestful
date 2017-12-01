package org.qfox.jestful.commons;

/**
 * Created by yangchangpei on 17/3/29.
 */
public interface Lock {

    void lockOne();

    void lockOne(long timeout);

    void lockOne(long timeout, int nanos);

    void openAny();

    void openAll();

    void doWithLock(LockBlock block);

}
