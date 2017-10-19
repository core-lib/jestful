package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.retry.RetryController;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.core.annotation.Jestful;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by yangchangpei on 17/9/27.
 */
@Jestful("/")
public interface UserAPI {

    UserAPI BIO = Client.builder()
            .setProtocol("https")
            .setHost("api.github.com")
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> false, 1))
            .create(UserAPI.class);

    UserAPI NIO = NioClient.builder()
            .setProtocol("https")
            .setHost("api.github.com")
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(UserAPI.class);

    UserAPI AIO = AioClient.builder()
            .setProtocol("https")
            .setHost("api.github.com")
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> true, 1))
            .create(UserAPI.class);

    @GET("/default")
    User user(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/default")
    Future<User> userOfFuture(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/default")
    Observable<User> userOfObservable(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/default")
    void user(@Header(value = "Authorization", encoded = true) String authorization, Callback<User> callback);

}
