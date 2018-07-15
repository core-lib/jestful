package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Matrix;
import org.qfox.jestful.core.http.Path;
import org.springframework.stereotype.Controller;

import java.util.Map;

@HTTP("/")
@Controller
public class IndexController {

    @GET("/")
    public String index() {
        return "@forward:/index.jsp";
    }

    @GET("/{criteria}/{other}")
    public String matrix(
            @Path("other") String other,
            @Path("criteria") String category,
            @Matrix("*") Map<String, String> name,
            @Matrix("age") int[] ages
    ) {

        return "@:OK";
    }

}
