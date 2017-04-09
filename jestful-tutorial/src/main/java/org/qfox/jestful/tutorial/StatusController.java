package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

/**
 * Created by Payne on 2017/4/9.
 */
@Jestful("/")
@Controller
public class StatusController {

    @GET("/204")
    public String do204() {
        return "status: 307 Found {Location: https://www.baidu.com}";
    }

}
