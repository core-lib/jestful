package org.qfox.jestful.sample;

import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.core.http.*;
import org.qfox.jestful.sample.user.User;

@HTTP("/redirect")
public interface RedirectSampleAPI {

    @GET("/source")
    User source();

    @GET("/target")
    void source(OnCompleted<User> callback);

    @POST("/source")
    User source(@Query("job") String job, @Body User user);

    @POST("/source")
    void source(@Query("job") String job, @Body User user, OnCompleted<User> callback);

}
