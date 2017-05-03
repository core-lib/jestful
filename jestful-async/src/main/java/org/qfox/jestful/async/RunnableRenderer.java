package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;
import org.qfox.jestful.server.renderer.Renderer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class RunnableRenderer implements Renderer, Initialable, Destroyable {
    private ExecutorService executor;

    @Override
    public boolean supports(Action action, Object value) {
        HttpServletRequest request = (HttpServletRequest) action.getRequest();
        if (request.isAsyncSupported() && value instanceof Runnable) {
            Mapping mapping = action.getMapping();
            Resource resource = action.getResource();
            Async async = mapping.isAnnotationPresent(Async.class)
                    ? mapping.getAnnotation(Async.class)
                    : resource.isAnnotationPresent(Async.class)
                    ? resource.getAnnotation(Async.class)
                    : null;
            return async == null || async.value();
        } else {
            return false;
        }
    }

    @Override
    public void render(Action action, Object value, Request request, Response response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.start((Runnable) value);
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void destroy() {
        if (executor != null) executor.shutdown();
    }

}
