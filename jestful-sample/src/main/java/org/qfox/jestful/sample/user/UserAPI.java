package org.qfox.jestful.sample.user;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.retry.RetryController;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Header;
import org.qfox.jestful.core.annotation.Jestful;

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
            .addForePlugins(new RetryController((action, thrown, result, exception) -> thrown, 3))
            .create(UserAPI.class);

    UserAPI NIO = NioClient.builder()
            .setProtocol("https")
            .setHost("api.github.com")
            .build()
            .creator()
            .addForePlugins(new RetryController((action, thrown, result, exception) -> thrown, 3))
            .create(UserAPI.class);

    @GET("/user")
    User user(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/user")
    Future<User> userOfFuture(@Header(value = "Authorization", encoded = true) String authorization);

    @GET("/user")
    void user(@Header(value = "Authorization", encoded = true) String authorization, Callback<User> callback);

}
