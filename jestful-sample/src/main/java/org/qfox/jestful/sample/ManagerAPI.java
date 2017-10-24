package org.qfox.jestful.sample;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Query;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by Payne on 2017/10/19.
 */
@Jestful("/manager")
public interface ManagerAPI {

    @GET("/html")
    String index(@Query("param") String param);

    @GET("/html")
    Future<String> indexOfFuture();

    @GET("/html")
    Observable<String> indexOfObservable();

    @GET("/html")
    void index(Callback<String> callback);

}
