package org.qfox.jestful.sample;


import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;

@Jestful("/cache")
@Controller
public class CacheController {

    @GET("/no-cache")
    public String noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return "@forward:/index.jsp";
    }

}
