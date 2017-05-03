package org.qfox.jestful.async;

import org.qfox.jestful.async.annotation.Async;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.renderer.Renderer;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class CallableRenderer implements Renderer, Initialable, Destroyable {
    private ExecutorService executor;
    private final Map<MediaType, ResponseSerializer> serializers = new HashMap<MediaType, ResponseSerializer>();

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
        executor.execute(new AsyncRunnable(asyncContext, (Callable<?>) value));
    }

    @Override
    public void destroy() {
        if (executor != null) executor.shutdown();
    }

    private class AsyncRunnable implements Runnable {
        private final AsyncContext asyncContext;
        private final Callable<?> callable;

        public AsyncRunnable(AsyncContext asyncContext, Callable<?> callable) {
            this.asyncContext = asyncContext;
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                Object value = callable.call();
                Response response = (Response) asyncContext.getResponse();

                String charset = response.getResponseHeader("Content-Charset");
                String contentType = response.getContentType();
                if (contentType != null) {
                    MediaType mediaType = MediaType.valueOf(contentType);
                    ResponseSerializer serializer = serializers.get(mediaType);
                    OutputStream out = response.getResponseOutputStream();
                    serializer.serialize(value, mediaType, charset, out);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                asyncContext.complete();
            }
        }
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Collection<ResponseSerializer> serializers = beanContainer.find(ResponseSerializer.class).values();
        for (ResponseSerializer serializer : serializers) {
            String contentType = serializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.serializers.put(mediaType, serializer);
        }

        executor = Executors.newCachedThreadPool();
    }

}
