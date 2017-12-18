package org.qfox.jestful.sample;


import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.Header;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;

@Jestful("/cache")
@Controller
public class CacheController {

    @GET("/no-cache")
    public String noCache(@Header("If-Modified-Since") String lastModified,
                          @Header("If-None-Match") String eTag,
                          HttpServletResponse response) {
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Last-Modified", "Mon, 13 Nov 2017 20:19:12 GMT");
        if (lastModified != null || eTag != null) return "@status: 304 Not Modified";
        return "@forward:/index.jsp";
    }

}
