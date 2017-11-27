package org.qfox.jestful.sample.user;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

@Jestful("/")
public interface BaiduAPI {

    @GET("/")
    String index();

}
