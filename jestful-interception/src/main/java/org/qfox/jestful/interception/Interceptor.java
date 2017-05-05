package org.qfox.jestful.interception;

/**
 * Created by yangchangpei on 17/4/24.
 */
public interface Interceptor {

    Object intercept(Invocation invocation) throws Exception;

}
