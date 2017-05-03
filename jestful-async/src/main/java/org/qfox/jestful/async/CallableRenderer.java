package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.renderer.Renderer;
import org.qfox.jestful.server.renderer.ResultRenderer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class CallableRenderer extends ResultRenderer implements Renderer, Initialable, Destroyable {
    private ExecutorService executor;

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
        executor.execute(new AsyncRunnable(asyncContext, (Callable<?>) value, action));
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {

    }

    public void initialize(BeanContainer beanContainer) {
        Collection<ResponseSerializer> serializers = beanContainer.find(ResponseSerializer.class).values();
        for (ResponseSerializer serializer : serializers) {
            String contentType = serializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.serializers.put(mediaType, serializer);
        }

        this.renderers.addAll(beanContainer.find(Renderer.class).values());
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void destroy() {
        if (executor != null) executor.shutdown();
    }

    private class AsyncRunnable implements Runnable, Actor {
        private final AsyncContext asyncContext;
        private final Callable<?> callable;
        private final Action action;
        private Object value;

        public AsyncRunnable(AsyncContext asyncContext, Callable<?> callable, Action action) {
            this.asyncContext = asyncContext;
            this.callable = callable;
            this.action = action;
        }

        @Override
        public void run() {
            try {
                value = callable.call();
                action.intrude(this);
                action.getResult().setRendered(false);
                CallableRenderer.this.react(action);
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
