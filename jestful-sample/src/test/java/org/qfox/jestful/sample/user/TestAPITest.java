package org.qfox.jestful.sample.user;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.TestAPI;

import java.util.concurrent.Future;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class TestAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        User user = TestAPI.AIO.user("Basic " + Base64.encode("core-lib:wan20100101"));
        System.out.println(user);

        Future<User> future = TestAPI.AIO.userOfFuture("Basic " + Base64.encode("core-lib:wan20100101"));
        User map = future.get();
        System.out.println(map);

        TestAPI.AIO.userOfObservable("Basic " + Base64.encode("core-lib:wan20100101"))
                .subscribe(System.out::println);
    }

    @Test
    public void getUserAsynchronously() throws Exception {
        final Lock lock = new SimpleLock();
        TestAPI.AIO.user("Basic " + Base64.encode("core-lib:wan20100101"), new Callback<User>() {
            @Override
            public void onCompleted(boolean success, User result, Exception exception) {
                lock.openAll();
            }

            @Override
            public void onSuccess(User result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }
        });
        lock.lockOne();
    }

    @Test
    public void getUserByHttpClient() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpClientContext context = new HttpClientContext();
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(new HttpHost("api.github.com", 443, "https")), new UsernamePasswordCredentials("core-lib", "wan20100101"));
        context.setCredentialsProvider(provider);

        AuthCache authCache = new BasicAuthCache();
        authCache.put(new HttpHost("api.github.com", 443, "https"), new BasicScheme());
        context.setAuthCache(authCache);

        HttpGet request = new HttpGet("https://api.github.com/user");
        HttpResponse response = client.execute(request, context);
        HttpEntity entity = response.getEntity();
        System.out.println(entity);
    }

}