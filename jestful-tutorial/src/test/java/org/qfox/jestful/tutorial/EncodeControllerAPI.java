package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Query;

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

}
