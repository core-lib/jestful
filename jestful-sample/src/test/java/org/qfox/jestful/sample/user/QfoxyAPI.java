package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.cache.impl.http.annotation.CacheControl;
import org.qfox.jestful.client.cache.impl.http.annotation.NoCache;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;

@Jestful("/")
public interface QfoxyAPI {

    @GET("/")
    String index();

    @GET("/js/jquery.js")
    String jQuery();

    @CacheControl(noCache = true)
    @GET("/hunter/alog/dp.min.js?v=-17521-17521")
    void jQuery(Callback<String> callback, @NoCache boolean noCache);

}
