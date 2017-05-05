package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class CallableRenderer extends AsyncRenderer {
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

        AsyncContext context = req.startAsync(req, resp);

        Mapping mapping = action.getMapping();
        Resource resource = action.getResource();
        Async async = mapping.isAnnotationPresent(Async.class)
                ? mapping.getAnnotation(Async.class)
                : resource.isAnnotationPresent(Async.class)
                ? resource.getAnnotation(Async.class)
                : null;

        context.setTimeout(async != null ? async.timeout() : 0);

        executor.execute(new Task(context, (Callable<?>) value, action));
    }

    public void initialize(BeanContainer beanContainer) {
        super.initialize(beanContainer);
        this.renderer = (Actor) beanContainer.get("renderer");
    }

    private class Task implements Runnable, Actor {
        private final AsyncContext asyncContext;
        private final Callable<?> callable;
        private final Action action;
        private final Result result;
        private final Body body;
        private Object value;

        public Task(AsyncContext asyncContext, Callable<?> callable, Action action) {
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
            } catch (StatusException exception) {
                try {
                    logger.error(exception.getMessage(), exception);
                    HttpServletRequest httpServletRequest = (HttpServletRequest) asyncContext.getRequest();
                    HttpServletResponse httpServletResponse = (HttpServletResponse) asyncContext.getResponse();
                    httpServletRequest.setAttribute("exception", exception);
                    httpServletResponse.sendError(exception.getStatus(), exception.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            } catch (Exception exception) {
                try {
                    logger.error(exception.getMessage(), exception);
                    HttpServletRequest httpServletRequest = (HttpServletRequest) asyncContext.getRequest();
                    HttpServletResponse httpServletResponse = (HttpServletResponse) asyncContext.getResponse();
                    httpServletRequest.setAttribute("exception", exception);
                    httpServletResponse.sendError(500, exception.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            } finally {
                if (asyncContext.getRequest().isAsyncStarted()) asyncContext.complete();
            }
        }

        @Override
        public Object react(Action action) throws Exception {
            return value;
        }

    }

}
