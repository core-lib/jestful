package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Query;

/**
 * Created by yangchangpei on 17/3/31.
 */
@Jestful("/cookie")
public interface LambdaAPI<R extends String> {

    @GET("/")
    void call(@Query("name") String name, OnSuccess<R> onSuccess, OnFail onFail, OnCompleted<R> onCompleted);

    @GET("/")
    void index(@Query("name") String name, OnSuccess<String> onSuccess, OnFail onFail, OnCompleted<String> onCompleted);

}
