package org.qfox.jestful.sample.user;

import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;

@Jestful("/")
public interface QfoxyAPI {

    @GET("/")
    String index();

    @GET("/js/jquery.js")
    String jQuery();

}
