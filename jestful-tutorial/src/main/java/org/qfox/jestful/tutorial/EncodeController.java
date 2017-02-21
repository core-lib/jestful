package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Query;
import org.springframework.stereotype.Controller;

/**
 * Created by yangchangpei on 17/2/21.
 */
@Jestful("/encode")
@Controller
public class EncodeController {

    @GET("/test")
    public String test(@Query(value = "decoded", decoded = true) String decoded, @Query(value = "notDecoded", decoded = false) String notDecoded,@Query(value = "名字", decoded = false) String name) {
        System.out.println(decoded);
        System.out.println(notDecoded);
        System.out.println(name);
        return "@:";
    }

}

