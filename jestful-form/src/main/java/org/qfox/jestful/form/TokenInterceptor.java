package org.qfox.jestful.form;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.annotation.DELETE;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.POST;
import org.qfox.jestful.core.annotation.PUT;
import org.qfox.jestful.interception.Interceptor;
import org.qfox.jestful.interception.Invocation;

import javax.servlet.ServletRequest;

/**
 * Created by yangchangpei on 17/8/23.
 */
public class TokenInterceptor implements Interceptor, Initialable {
    private String name = "form-token";
    private TokenManager tokenManager;

    @Override
    public void initialize(BeanContainer beanContainer) {
        tokenManager = beanContainer.get(TokenManager.class);
    }

    @GET("/.*")
    @POST("/.*")
    @PUT("/.*")
    @DELETE("/.*")
    @Override
    public Object intercept(Invocation invocation) throws Exception {
        boolean needed = invocation.getMapping().isAnnotationPresent(TokenValidate.class);
        if (needed) {
            ServletRequest request = (ServletRequest) invocation.getRequest();
            String token = request.getParameter(name);
            if (token == null) throw new TokenRequiredException();
            tokenManager.verify(token);
        }
        return invocation.invoke();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
