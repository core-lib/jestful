package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;
import org.qfox.jestful.sample.user.User;

@Jestful("/redirect")
public interface RedirectSampleAPI {

    @GET("/source")
    User source();

    @POST("/source")
    User source(@Body User user);

}
