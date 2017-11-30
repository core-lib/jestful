package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;
import org.springframework.stereotype.Controller;

@Controller
@Jestful("/")
public class IndexController {

    @GET("/")
    public String index() {
        return "@forward:/index.jsp";
    }

}
