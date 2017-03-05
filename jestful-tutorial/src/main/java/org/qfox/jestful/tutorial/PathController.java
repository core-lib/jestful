package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Path;
import org.springframework.stereotype.Controller;

/**
 * Created by payne on 2017/3/5.
 */
@Jestful("/path/{path:\\w+}")
@Controller
public class PathController {

    @GET("/")
    public String get(@Path("path") String path) {
        return "@:" + path;
    }

}
