package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

/**
 * Created by payne on 2017/3/5.
 */
@Jestful("/encode")
@Controller
public class EncodeController2 {

    @GET("/sdfsdf")
    public String sdfsdf(){
        return "@:sdf";
    }

}
