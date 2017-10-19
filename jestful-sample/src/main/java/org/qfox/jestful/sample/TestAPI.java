package org.qfox.jestful.sample;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.retry.RetryController;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.sample.user.User;
import org.qfox.jestful.sample.user.UserAPI;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by Payne on 2017/10/19.
 */
public interface TestAPI {

    UserAPI BIO = Client.builder()
            .setProtocol("http")
            .setHost("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> false, 1))
            .create(UserAPI.class);

    UserAPI NIO = NioClient.builder()
            .setProtocol("http")
            .setHost("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(UserAPI.class);

    UserAPI AIO = AioClient.builder()
            .setProtocol("http")
            .setHost("localhost")
            .setPort(8080)
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(UserAPI.class);

    @GET("/user")
    User user(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/user")
    Future<User> userOfFuture(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/user")
    Observable<User> userOfObservable(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/user")
    void user(@Header(value = "Authorization", encoded = true) String authorization, Callback<User> callback);

}
