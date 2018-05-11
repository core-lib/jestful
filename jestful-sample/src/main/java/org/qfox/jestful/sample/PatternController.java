package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.springframework.stereotype.Controller;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-11 9:55
 **/
@HTTP("")
@Controller
public class PatternController {

    @GET("")
    public String index(@Path("pattern") String pattern) {

        return "@:ok";
    }

}
