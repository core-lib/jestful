package org.qfox.jestful.sample.user;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.sample.ManagerAPI;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class ManagerAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        AuthManager manager = new AuthManager();
        AuthStorage authStorage = new SimpleAuthStorage();
//        authStorage.put(new Host("http", "localhost", 8080), new BasicScheme());
        manager.setAuthStorage(authStorage);

        CredenceProvider credenceProvider = new SimpleCredenceProvider();
        credenceProvider.setCredence(new Scope("basic", Scope.ANY_REALM, "localhost", 8080), new SimpleCredence("tomcat", "tomcat"));
        manager.setCredenceProvider(credenceProvider);

        manager.setHostNormalizer(SimpleHostNormalizer.INSTANCE);
        manager.setSchemeFactoryRegistry(new SimpleSchemeFactoryRegistry());

        ManagerAPI managerAPI = Client.builder()
                .setProtocol("http")
                .setHost("localhost")
                .setPort(8080)
                .build()
                .creator()
                .addForePlugins(manager)
                .create(ManagerAPI.class);

        String html = managerAPI.index();
        System.out.println(html);
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
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(new HttpHost("localhost", 8080, "http")), new UsernamePasswordCredentials("tomcat", "tomcat"));
        context.setCredentialsProvider(provider);

        org.apache.http.client.AuthCache authAuthCache = new BasicAuthCache();
        authAuthCache.put(new HttpHost("localhost", 8080, "http"), new DigestScheme());
        context.setAuthCache(authAuthCache);

        HttpGet request = new HttpGet("http://localhost:8080/manager/html");
        HttpResponse response = client.execute(request, context);
        response.getEntity().writeTo(System.out);
    }

}