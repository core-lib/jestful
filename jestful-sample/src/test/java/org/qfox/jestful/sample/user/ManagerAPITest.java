package org.qfox.jestful.sample.user;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.CredenceProvider;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.ManagerAPI;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class ManagerAPITest {

    // Digest realm="Tomcat Manager Application", qop="auth", nonce="1508767262604:e176001459d8327b75fa057fc8c123af", opaque="05B63BC01E8813AE9958DA045B35EF92"
    @Test
    public void getUserSynchronously() throws Exception {
        Authenticator authenticator = new Authenticator();

        CredenceProvider credenceProvider = authenticator.getCredenceProvider();
        credenceProvider.setCredence(new Scope(Scope.ANY_SCHEME, Scope.ANY_REALM, "localhost", 8080), new SimpleCredence("tomcat", "tomcat"));

        ManagerAPI managerAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .build()
                .creator()
                .addBackPlugins(authenticator)
                .create(ManagerAPI.class);

        {
            String html = managerAPI.index("中文");
            System.out.println(html);
        }

        {
            final Lock lock = new SimpleLock();
            managerAPI.index(new Callback<String>() {

                @Override
                public void onCompleted(boolean success, String result, Exception exception) {
                    lock.openAll();
                }

                @Override
                public void onSuccess(String result) {
                    System.out.println(result);
                }

                @Override
                public void onFail(Exception exception) {

                }
            });
            lock.lockOne();
        }
    }

    // Authorization: Digest
    // username=tomcat,
    // realm="Tomcat Manager Application",
    // nonce="1508638890982:7cd64d37195d5108518e837a3cf11aea",
    // uri="/manager/html",
    // cnonce=7f464021948af31c7192d925f817efbf,
    // nc=00000002,
    // response=c10e7c70c84b4e0564b6a33f08efcc1d,
    // qop=auth,
    // opaque=7B5191D742A44629F4E8163D29E74B95

    // Authorization: Digest
    // username=tomcat,
    // realm="Tomcat Manager Application",
    // nonce="1508638890982:7cd64d37195d5108518e837a3cf11aea",
    // uri="/manager/html",
    // cnonce=7f464021948af31c7192d925f817efbf,
    // nc=00000003,
    // response=9898bc76f20b1b043cfb3360efb2cca7,
    // qop=auth,
    // opaque=7B5191D742A44629F4E8163D29E74B95
    @Test
    public void getUserByHttpClient() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpClientContext context = new HttpClientContext();
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(new HttpHost("localhost", 8080, "http")), new UsernamePasswordCredentials("tomcat", "tomcat"));
        context.setCredentialsProvider(provider);
        org.apache.http.client.AuthCache authAuthCache = new BasicAuthCache();
//        authAuthCache.put(new HttpHost("api.github.com", 443, "https"), new org.apache.http.impl.auth.BasicScheme());
        context.setAuthCache(authAuthCache);

        {
            HttpGet request = new HttpGet("http://localhost:8080/manager/html?param=中文");
            HttpResponse response = client.execute(request, context);
            response.getEntity().writeTo(System.out);
        }

        {
            HttpGet request = new HttpGet("http://localhost:8080/manager/html");
            HttpResponse response = client.execute(request, context);
            response.getEntity().writeTo(System.out);
        }
    }

}