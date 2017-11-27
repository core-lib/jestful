package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

@Controller
@Jestful("/")
public class IndexController {

    @GET("/")
    public String index() {
        return "@forward:/index.jsp";
    }

}
