package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.Message;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

/**
 * Created by yangchangpei on 17/3/13.
 */
@Jestful("/message")
public interface MessageControllerAPI {

    @GET("/success")
    Message<String> success();

    @GET("/fail")
    Message<String> fail();

    @GET("/fail")
    String realyFail();

}
