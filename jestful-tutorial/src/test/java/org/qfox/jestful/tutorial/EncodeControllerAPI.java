package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.annotation.Headers;
import org.qfox.jestful.core.annotation.*;

/**
 * Created by yangchangpei on 17/2/21.
 */
@Jestful("/encode")
public interface EncodeControllerAPI {

    @GET("/testQuery")
    String testQuery(@Query(value = "decoded", decoded = true) String decoded,
                     @Query(value = "notDecoded", decoded = false) String notDecoded,
                     @Query(value = "名字", decoded = true) String name);

    @GET("/testHeader")
    String testHeader(@Header(value = "decoded", decoded = true) String decoded,
                      @Header(value = "notDecoded", decoded = false) String notDecoded,
                      @Header(value = "名字", decoded = true) String name);

    @GET("/testHeader")
    @Headers(value = {"decoded:杨昌沛", "notDecoded:杨昌沛", "名字:杨昌沛"})
    String testHeader2();

    @GET("/testCookie")
    String testCookie(@Cookie(value = "decoded", decoded = true) String decoded,
                      @Cookie(value = "notDecoded", decoded = false) String notDecoded,
                      @Cookie(value = "名字", decoded = true) String name);

    @POST(value = "/testBody", consumes = "application/x-www-form-urlencoded")
    String testBody(@Body("sdf") String body);

}
