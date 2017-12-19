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

    @GET("/hunter/alog/monkey.min.js")
    void jQuery(Callback<String> callback);

}
