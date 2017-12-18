package org.qfox.jestful.sample;


import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.Header;
import org.qfox.jestful.core.http.POST;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;

@Jestful("/cache")
@Controller
public class CacheController {

    @GET("/get")
    public String get(@Header("If-Modified-Since") String lastModified,
                      @Header("If-None-Match") String eTag,
                      HttpServletResponse response) {
        System.out.println(lastModified + ":" + eTag);
        response.setHeader("Cache-Control", "max-age=100, must-revalidate");
        response.setHeader("Last-Modified", "Mon, 13 Nov 2017 20:19:12 GMT");
        response.setHeader("ETag", "v1.0");
        if (lastModified != null || eTag != null) return "@status: 304 Not Modified";
        return "@forward:/index.jsp";
    }

    @POST("/post")
    public String get(@Header("If-Modified-Since") String lastModified,
                      @Header("If-None-Match") String eTag,
                      @Body("name") String name,
                      HttpServletResponse response) {
        System.out.println(name + ":" + lastModified + ":" + eTag);
        response.setHeader("Cache-Control", "max-age=100, must-revalidate");
        response.setHeader("Last-Modified", "Mon, 13 Nov 2017 20:19:12 GMT");
        response.setHeader("ETag", "v1.0");
        if (lastModified != null || eTag != null) return "@status: 304 Not Modified";
        return "@forward:/index.jsp";
    }

}
