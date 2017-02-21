package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.*;
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

    @GET("/testCookie")
    public String testCookie(@Cookie(value = "decoded", decoded = true) String decoded,
                             @Cookie(value = "notDecoded", decoded = false) String notDecoded,
                             @Cookie(value = "名字", decoded = true) String name) {
        System.out.println(decoded);
        System.out.println(notDecoded);
        System.out.println(name);
        return "@:";
    }

    @GET("/testPath/{path:.*}")
    public String testPath(@Path(value = "path") String path) {
        System.out.println(path);
        return "@:";
    }

    @POST("/testBody")
    public String testBody(@Body(value = "名字", decoded = true) String body) {
        System.out.println(body);
        return "@:";
    }

}

