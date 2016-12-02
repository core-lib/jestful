package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

/**
 * Created by yangchangpei on 16/11/29.
 */
@Jestful("/include")
@Controller
public class IncludeController {

    @GET("/jsp")
    public String jsp() {
        return "forward:/include.jsp";
    }

    @GET("/text")
    public String text() {
        return "@:text";
    }

    @GET(value = "/json", produces = "application/json")
    public WechatAccessResult json() {
        return new WechatAccessResult();
    }

    @GET(value = "/xml", produces = "application/xml")
    public WechatAccessResult xml() {
        return new WechatAccessResult();
    }

    @GET(value = "/java", produces = "application/x-java-serialized-object")
    public String java() {
        return "sdfsdfsdf";
    }

}
