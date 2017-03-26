package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

import java.io.File;

/**
 * Created by yangchangpei on 17/3/24.
 */
@Jestful("/")
public interface ProxyAPI {

    @GET("/httpgallery/chunked/chunkedimage.aspx")
    String index();

    @GET("/httpgallery/chunked/chunkedimage.aspx")
    void index(Callback<String> callback);

}
