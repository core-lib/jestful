package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;

@HTTP("/")
public class IndexController {

    @GET("/")
    public String index() {
        return "@forward:/index.jsp";
    }

}
