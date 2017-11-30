package org.qfox.jestful.sample.repository;

import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.Path;

import java.util.List;

/**
 * Created by yangchangpei on 17/9/27.
 */
@Jestful("/")
public interface RepositoryAPI {

    @GET("/users/{username:.+}/repos")
    List<Repository> list(@Path("username") String username);

}
