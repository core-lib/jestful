package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class RunnableRenderer extends AsyncRenderer {

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

        AsyncContext context = req.startAsync(req, resp);

        Mapping mapping = action.getMapping();
        Resource resource = action.getResource();
        Async async = mapping.isAnnotationPresent(Async.class)
                ? mapping.getAnnotation(Async.class)
                : resource.isAnnotationPresent(Async.class)
                ? resource.getAnnotation(Async.class)
                : null;

        context.setTimeout(async != null ? async.timeout() : 0);

        executor.execute(new Task(context, (Runnable) value));
    }

    private class Task implements Runnable {
        private final AsyncContext asyncContext;
        private final Runnable runnable;

        public Task(AsyncContext asyncContext, Runnable runnable) {
            this.asyncContext = asyncContext;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception exception) {
                try {
                    logger.error(exception.getMessage(), exception);
                    asyncContext.getRequest().setAttribute("exception", exception);
                    ((HttpServletResponse) asyncContext.getResponse()).sendError(500, exception.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    if (asyncContext.getRequest().isAsyncStarted()) asyncContext.complete();
                }
            }
        }

    }

}
