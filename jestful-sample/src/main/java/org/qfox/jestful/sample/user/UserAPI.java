package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.core.annotation.Jestful;

/**
 * Created by yangchangpei on 17/9/27.
 */
@Jestful("/")
public interface UserAPI {

    UserAPI INSTANCE = Client.builder()
            .setProtocol("https")
            .setHost("api.github.com")
            .build()
            .create(UserAPI.class);

    @GET("/user")
    String user(@Header(value = "Authorization", encoded = true) String authorization);

}
