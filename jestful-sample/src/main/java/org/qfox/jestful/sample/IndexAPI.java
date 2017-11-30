package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;

@Jestful("/")
public interface IndexAPI {

    @GET("/")
    String index();

}
