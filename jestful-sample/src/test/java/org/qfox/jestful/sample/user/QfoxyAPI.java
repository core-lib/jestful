package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.cache.impl.http.annotation.NoCache;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;

@HTTP("/")
public interface QfoxyAPI {

    @GET("/")
    String index();

    @GET("/js/jquery.js")
    String jQuery();

    @GET("/hunter/alog/dp.min.js?v=-17521-17521")
    void jQuery(Callback<String> callback, @NoCache boolean noCache);

}
