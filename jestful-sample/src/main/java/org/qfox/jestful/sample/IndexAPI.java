package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

@Jestful("/")
public interface IndexAPI {

    @GET("/")
    String index();

}
