package org.qfox.jestful.sample.authorization;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;

/**
 * Created by yangchangpei on 17/9/27.
 */
@Jestful("/")
public interface AuthorizationAPI {

    @POST("/authorizations")
    Authorization create(@Body Authorization authorization);

}
