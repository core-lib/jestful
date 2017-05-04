package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.server.renderer.Renderer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class CallableRenderer implements Renderer, Initialable, Destroyable {
    private ExecutorService executor;
    private Actor renderer;

    @Override
    public boolean supports(Action action, Object value) {
        if (value instanceof Callable) {
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

        Mapping mapping = action.getMapping();
        Resource resource = action.getResource();
        Async async = mapping.isAnnotationPresent(Async.class)
                ? mapping.getAnnotation(Async.class)
                : resource.isAnnotationPresent(Async.class)
                ? resource.getAnnotation(Async.class)
                : null;
        asyncContext.setTimeout(async != null ? async.timeout() : 0);

        executor.execute(new AsyncRunnable(asyncContext, (Callable<?>) value, action));
    }

    public void initialize(BeanContainer beanContainer) {
        this.executor = Executors.newCachedThreadPool();
        this.renderer = (Actor) beanContainer.get("renderer");
    }

    @Override
    public void destroy() {
        if (executor != null) executor.shutdown();
    }

    private class AsyncRunnable implements Runnable, Actor {
        private final AsyncContext asyncContext;
        private final Callable<?> callable;
        private final Action action;
        private final Result result;
        private final Body body;
        private Object value;

        public AsyncRunnable(AsyncContext asyncContext, Callable<?> callable, Action action) {
            this.asyncContext = asyncContext;
            this.callable = callable;
            this.action = action;
            this.result = action.getResult();
            this.body = result.getBody();
        }

        @Override
        public void run() {
            try {
                value = callable.call();
                body.setValue(value);
                action.intrude(this);
                action.getResult().setRendered(false);
                renderer.react(action);
            } catch (Exception e) {
                throw new JestfulRuntimeException(e);
            } finally {
                asyncContext.complete();
            }
        }

        @Override
        public Object react(Action action) throws Exception {
            return value;
        }
    }

}
