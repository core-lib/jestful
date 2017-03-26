package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Path;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by payne on 2017/3/26.
 */
@Jestful("/products")
public interface NioProductAPI {

    @GET(value = "/{productId:\\d+}.json", produces = "application/json")
    void json(@Path("productId") Long productId, Callback<List<Long>> callback);

    @GET(value = "/{productId:\\d+}.json", produces = "application/json")
    Future<List<Long>> json(@Path("productId") Long productId);

}
