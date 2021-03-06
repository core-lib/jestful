package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.sample.IndexAPI;

public class IndexAPITests {

    @Test
    public void index() throws Exception {
        Authenticator authenticator = Authenticator.builder().build();
        authenticator.getCredenceProvider().setCredence(new Scope("api.github.com", Scope.ANY_PORT), new SimpleCredence("core-lib", "wan20100101"));

        IndexAPI indexAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setRoute("/jestful-sample")
                .setKeepAlive(true)
                .build()
                .creator()
                .setBackPlugins(authenticator)
                .addBackPlugins(Redirector.builder().build())
                .create(IndexAPI.class);

        for (int i = 0; i < 10; i++) {
            String index = indexAPI.index();
            System.out.println(index);
        }
    }

    @Test
    public void test() throws Exception {
        IndexAPI indexAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setRoute("/jestful-sample")
                .setKeepAlive(true)
                .build()
                .creator()
                .addBackPlugins(Redirector.builder().build())
                .create(IndexAPI.class);
        indexAPI.matrix(
                "中文路径",
                "中文矩阵",
                new String[]{"姓名1", "姓名2"},
                new String[]{"中文查询1", "中文查询2"},
                new String[]{"中文请求头1", "中文请求头1"},
                new String[]{"中文饼干1", "中文饼干1"}
        );
    }

}
