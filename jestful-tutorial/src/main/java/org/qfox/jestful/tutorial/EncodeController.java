package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Query;
import org.springframework.stereotype.Controller;

/**
 * Created by yangchangpei on 17/2/21.
 */
@Jestful("/encode")
@Controller
public class EncodeController {

    @GET("/testQuery")
    public String testQuery(@Query(value = "decoded", decoded = true) String decoded,
                            @Query(value = "notDecoded", decoded = false) String notDecoded,
                            @Query(value = "名字", decoded = true) String name) {
        System.out.println(decoded);
        System.out.println(notDecoded);
        System.out.println(name);
        return "@:";
    }

    @GET("/testHeader")
    public String testHeader(@Header(value = "decoded", decoded = true) String decoded,
                             @Header(value = "notDecoded", decoded = false) String notDecoded,
                             @Header(value = "名字", decoded = true) String name) {
        System.out.println(decoded);
        System.out.println(notDecoded);
        System.out.println(name);
        return "@:";
    }

}

