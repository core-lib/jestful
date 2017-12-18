package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;

@Jestful("/cache")
public interface CacheAPI {

    @GET("/no-cache")
    String noCache();

    @GET("/no-cache")
    void noCache(Callback<String> callback);

}
