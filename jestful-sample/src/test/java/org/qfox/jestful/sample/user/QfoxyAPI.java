package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;

@Jestful("/")
public interface QfoxyAPI {

    @GET("/")
    String index();

    @GET("/js/jquery.js")
    String jQuery();

    @GET("/hunter/alog/speed.min.js?v=170721")
    void jQuery(Callback<String> callback);

}
