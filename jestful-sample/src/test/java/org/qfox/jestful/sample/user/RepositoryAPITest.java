package org.qfox.jestful.sample.user;

import org.junit.Before;
import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.CredenceProvider;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.sample.repository.Repository;
import org.qfox.jestful.sample.repository.RepositoryAPI;
import org.qfox.jestful.sample.repository.Topics;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RepositoryAPITest {
    private RepositoryAPI repositoryAPI;

    @Before
    public void setup() throws Exception {
        Authenticator authenticator = Authenticator.builder().build();
        CredenceProvider credenceProvider = authenticator.getCredenceProvider();
        credenceProvider.setCredence(new Scope("api.github.com", 443), new SimpleCredence("core-lib", "wan20100101"));
        repositoryAPI = Client.builder()
                .setProtocol("https")
                .setHostname("api.github.com")
                .setKeepAlive(false)
                .build()
                .creator()
                .addBackPlugins(authenticator)
                .create(RepositoryAPI.class);
    }

    @Test
    public void getMineRepositories() throws Exception {
        List<Repository> list = repositoryAPI.getMineRepositories();
        System.out.println(list);
    }

    @Test
    public void getUserRepositories() throws Exception {
        List<Repository> repositories = repositoryAPI.getUserRepositories("core-lib");
        System.out.println(repositories);

        repositories = repositoryAPI.getUserRepositories("MJ");
        System.out.println(repositories);
    }

    @Test
    public void getOrganizationRepositories() throws Exception {
        List<Repository> repositories = repositoryAPI.getOrganizationRepositories("all");
        System.out.println(repositories);
    }

    @Test
    public void getAllPublicRepositories() throws Exception {
        List<Repository> repositories = repositoryAPI.getAllPublicRepositories(null);
        System.out.println(repositories);

        repositories = repositoryAPI.getAllPublicRepositories((int) repositories.get(repositories.size() - 1).getId());
        System.out.println(repositories);
    }

    @Test
    public void create() throws Exception {
        Repository repository = new Repository();
        repository.setName("Testingdsafsdf");
        repository.setDescription("Description");
        repository.setHomepage("https://github.com");
        repository.setHas_issues(true);
        repository.setHas_projects(true);
        repository.setHas_wiki(false);
        repository.setTopics(Arrays.asList("123", "3333"));
        Repository created = repositoryAPI.create(repository);
        System.out.println(created);
    }

    @Test
    public void getUserRepository() throws Exception {
        Repository jestful = repositoryAPI.getUserRepository("core-lib", "jestful");
        System.out.println(jestful);
    }

    @Test
    public void getUserRepositoryTopics() throws Exception {
        try {
            Topics topics = repositoryAPI.getUserRepositoryTopics("core-lib", "jestful");
            System.out.println(topics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}