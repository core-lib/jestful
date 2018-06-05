package org.qfox.jestful.sample.repository;

import org.qfox.jestful.client.annotation.Headers;
import org.qfox.jestful.core.http.*;

import java.util.List;

/**
 * Created by yangchangpei on 17/9/27.
 */
@HTTP("/")
public interface RepositoryAPI {

    @GET("/user/repos")
    List<Repository> getMineRepositories();

    @GET("/users/{username:.+}/repos")
    List<Repository> getUserRepositories(@Path("username") String username);

    @GET("/orgs/{org:.+}/repos")
    List<Repository> getOrganizationRepositories(@Path("org") String org);

    @GET("/repositories")
    List<Repository> getAllPublicRepositories(@Query("since") Integer since);

    @POST(value = "/user/repos", consumes = "application/json")
    Repository create(@Body("*") Repository repository);

    @GET("/repos/{owner:.+}/{repo:.+}")
    Repository getUserRepository(@Path("owner") String owner, @Path("repo") String repository);

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("/repos/{owner:.+}/{repo:.+}/topics")
    Topics getUserRepositoryTopics(@Path("owner") String owner, @Path("repo") String repository);

}
