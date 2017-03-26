package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

/**
 * Created by payne on 2017/3/26.
 */
@Jestful("/cookie")
public interface CookieAPI {

    @GET("/")
    void index(Callback<String> callback);

}