package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;

@HTTP("/")
public interface IndexAPI {

    @GET("/")
    String index();

}
