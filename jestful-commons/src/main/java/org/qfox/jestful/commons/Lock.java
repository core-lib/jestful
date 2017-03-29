package org.qfox.jestful.commons;

/**
 * Created by yangchangpei on 17/3/29.
 */
public interface Lock {

    void lockOne();

    void openAny();

    void openAll();

    void doWithLock(LockBlock block) throws Exception;

}
