package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Path;

/**
 * Created by payne on 2017/3/5.
 */
@Jestful("/path/{path:.+}")
public interface PathControllerAPI {

    @GET("/{id:\\d+}")
    String get(@Path(value = "path", encoded = true) String path, @Path("id") Long id);

}
