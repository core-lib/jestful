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
@Jestful("/")
public interface ManagerAPI {

    @GET("/index.html")
    String index(@Query("param") String param);

    @GET("/index.html")
    Future<String> indexOfFuture();

    @GET("/index.html")
    Observable<String> indexOfObservable();

    @GET("/index.html")
    void index(Callback<String> callback);

}
