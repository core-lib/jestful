package org.qfox.jestful.client;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/10/16.
 */
public interface Promise {

    Object get() throws Exception;

    Object get(TimeUnit unit, long duration) throws Exception;



}
