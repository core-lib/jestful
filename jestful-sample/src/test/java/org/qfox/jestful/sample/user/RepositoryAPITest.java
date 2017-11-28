package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.sample.repository.Repository;
import org.qfox.jestful.sample.repository.RepositoryAPI;

import java.util.List;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RepositoryAPITest {

    @Test
    public void list() throws Exception {
        RepositoryAPI repositoryAPI = AioClient.builder()
                .setProtocol("https")
                .setHostname("api.github.com")
                .setKeepAlive(true)
                .build()
                .creator()
                .create(RepositoryAPI.class);
        List<Repository> repositories = repositoryAPI.list("core-lib");
        System.out.println(repositories);

        repositories = repositoryAPI.list("MJ");
        System.out.println(repositories);
    }

}