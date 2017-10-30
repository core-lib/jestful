package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

/**
 * Created by yangchangpei on 17/9/27.
 */
@Jestful("/")
public interface UserAPI {

    @GET("/user")
    User user();

    @GET("/user")
    void user(Callback<User> callback);

}
