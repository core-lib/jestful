package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.cache.impl.http.annotation.OnlyIfCached;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.POST;

@Jestful("/cache")
public interface CacheAPI {

    @GET("/get")
    String get();

    @GET("/get")
    void get(@OnlyIfCached boolean onlyIfCached, Callback<String> callback);

    @POST("/post")
    String post(@Body("name") String name);

    @POST("/post")
    void post(@Body("name") String name, Callback<String> callback);

}
