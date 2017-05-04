package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.server.exception.UnsupportedTypeException;

import java.util.*;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class ParameterResolver implements Actor, Initialable, Destroyable, Configurable {
    private final Map<MediaType, RequestDeserializer> deserializers = new HashMap<MediaType, RequestDeserializer>();
    private final List<Resolver> resolvers = new ArrayList<Resolver>();

    @Override
    public Object react(Action action) throws Exception {
        Parameters parameters = action.getParameters();

        for (Parameter p : parameters) for (Resolver r : resolvers) if (r.supports(action, p)) r.resolve(action, p);

        Restful restful = action.getRestful();
        if (!restful.isAcceptBody()) return action.execute();

        if (parameters.count(Position.BODY) == 0) return action.execute();

        Request request = action.getRequest();
        String contentType = request.getContentType();
        if (contentType == null || contentType.length() == 0) {
            Accepts consumes = action.getConsumes();
            if (consumes.size() == 1) contentType = consumes.iterator().next().getName();
            else return action.execute();
        }
        MediaType mediaType = MediaType.valueOf(contentType);

        Accepts consumes = action.getConsumes();
        Accepts supports = new Accepts(deserializers.keySet());
        if (supports.contains(mediaType) && (consumes.isEmpty() || consumes.contains(mediaType))) {
            String charset = mediaType.getCharset();
            if (charset == null || charset.length() == 0) charset = request.getRequestHeader("Content-Charset");
            if (charset == null || charset.length() == 0) charset = request.getCharacterEncoding();
            if (charset == null || charset.length() == 0) charset = java.nio.charset.Charset.defaultCharset().name();
            RequestDeserializer deserializer = deserializers.get(mediaType);
            deserializer.deserialize(action, mediaType, charset, request.getRequestInputStream());
        } else if (consumes.size() == 1) {
            String charset = mediaType.getCharset();
            mediaType = consumes.iterator().next();
            if (charset == null || charset.length() == 0) charset = request.getRequestHeader("Content-Charset");
            if (charset == null || charset.length() == 0) charset = mediaType.getCharset();
            if (charset == null || charset.length() == 0) charset = request.getCharacterEncoding();
            if (charset == null || charset.length() == 0) charset = java.nio.charset.Charset.defaultCharset().name();
            RequestDeserializer deserializer = deserializers.get(mediaType);
            deserializer.deserialize(action, mediaType, charset, request.getRequestInputStream());
        } else {
            String URI = action.getURI();
            String method = action.getRestful().getMethod();
            if (!consumes.isEmpty()) supports.retainAll(consumes);
            throw new UnsupportedTypeException(URI, method, mediaType, supports);
        }

        return action.execute();
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
        for (RequestDeserializer deserializer : deserializers) {
            String contentType = deserializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.deserializers.put(mediaType, deserializer);
        }

        this.resolvers.addAll(beanContainer.find(Resolver.class).values());
    }

    @Override
    public void destroy() {
        for (Resolver r : resolvers) if (r instanceof Destroyable) ((Destroyable) r).destroy();
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Resolver r : resolvers) if (r instanceof Configurable) ((Configurable) r).config(arguments);
    }
}
