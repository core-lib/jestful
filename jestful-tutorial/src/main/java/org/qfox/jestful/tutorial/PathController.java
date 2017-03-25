package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Path;
import org.springframework.stereotype.Controller;

/**
 * Created by payne on 2017/3/5.
 */
@Jestful("/path/{path:.+}")
@Controller
public class PathController {

    @GET("/{id:\\d+}")
    public String get(@Path("path") String path, @Path("id") Long id) {
        return "@:" + path + "," + id;
    }

}
