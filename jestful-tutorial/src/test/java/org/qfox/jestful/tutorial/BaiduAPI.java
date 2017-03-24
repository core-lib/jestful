package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

/**
 * Created by yangchangpei on 17/3/24.
 */
@Jestful("/")
public interface BaiduAPI {

    @GET("/index.html")
    public String index();

}
