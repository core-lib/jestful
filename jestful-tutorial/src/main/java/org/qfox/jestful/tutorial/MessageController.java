package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

/**
 * Created by yangchangpei on 17/3/13.
 */
@Jestful("/message")
@Controller
public class MessageController {

    @GET("/success")
    public String success() {
        return "@:success";
    }

    @GET("/fail")
    public String fail() {
        throw new RuntimeException("fail");
    }

}
