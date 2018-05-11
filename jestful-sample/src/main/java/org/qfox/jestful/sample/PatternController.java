package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.springframework.stereotype.Controller;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-11 9:55
 **/
@HTTP("a-{a}-b-{b:\\d+}")
@Controller
public class PatternController {

    @GET("c-{c:\\w+}-d-{d}")
    public String index(@Path("c") String c, @Path("a") String a, @Path("d") String d, @Path("b") String b) {

        return "@:ok";
    }

}
