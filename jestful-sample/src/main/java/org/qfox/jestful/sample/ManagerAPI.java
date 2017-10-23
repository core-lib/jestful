package org.qfox.jestful.sample;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.auth.AuthManager;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.retry.RetryController;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by Payne on 2017/10/19.
 */
@Jestful("/manager")
public interface ManagerAPI {

    ManagerAPI BIO = Client.builder()
            .setProtocol("http")
            .setHostname("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new AuthManager())
            .create(ManagerAPI.class);

    ManagerAPI NIO = NioClient.builder()
            .setProtocol("http")
            .setHostname("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(ManagerAPI.class);

    ManagerAPI AIO = AioClient.builder()
            .setProtocol("http")
            .setHostname("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(ManagerAPI.class);

    @GET("/html")
    String index();

    @GET("/html")
    Future<String> indexOfFuture();

    @GET("/html")
    Observable<String> indexOfObservable();

    @GET("/html")
    void index(Callback<String> callback);

}
