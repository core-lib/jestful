package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.server.exception.UnsupportedTypeException;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月8日 下午3:42:21
 * @since 1.0.0
 */
public class BodyParameterResolver implements Actor, Initialable {
    private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

    public Object react(Action action) throws Exception {
        Restful restful = action.getRestful();
        if (restful.isAcceptBody() == false) {
            return action.execute();
        }

        Parameters parameters = action.getParameters();
        if (parameters.count(Position.BODY) == 0) {
            return action.execute();
        }

        Request request = action.getRequest();
        String contentType = request.getContentType();
        if (contentType == null || contentType.length() == 0) {
            Accepts consumes = action.getConsumes();
            if (consumes.size() == 1) {
                contentType = consumes.iterator().next().getName();
            } else {
                return action.execute();
            }
        }
        MediaType mediaType = MediaType.valueOf(contentType);

        Accepts consumes = action.getConsumes();
        Accepts supports = new Accepts(map.keySet());
        if (supports.contains(mediaType) && (consumes.isEmpty() || consumes.contains(mediaType))) {
            String charset = mediaType.getCharset();
            if (charset == null || charset.length() == 0) {
                charset = request.getRequestHeader("Content-Charset");
            }
            if (charset == null || charset.length() == 0) {
                charset = request.getCharacterEncoding();
            }
            if (charset == null || charset.length() == 0) {
                charset = Charset.defaultCharset().name();
            }
            RequestDeserializer deserializer = map.get(mediaType);
            deserializer.deserialize(action, mediaType, charset, request.getRequestInputStream());
        } else if (consumes.size() == 1) {
            String charset = mediaType.getCharset();
            mediaType = consumes.iterator().next();
            if (charset == null || charset.length() == 0) {
                charset = request.getRequestHeader("Content-Charset");
            }
            if (charset == null || charset.length() == 0) {
                charset = mediaType.getCharset();
            }
            if (charset == null || charset.length() == 0) {
                charset = request.getCharacterEncoding();
            }
            if (charset == null || charset.length() == 0) {
                charset = Charset.defaultCharset().name();
            }
            RequestDeserializer deserializer = map.get(mediaType);
            deserializer.deserialize(action, mediaType, charset, request.getRequestInputStream());
        } else {
            String URI = action.getURI();
            String method = action.getRestful().getMethod();
            if (consumes.isEmpty() == false) {
                supports.retainAll(consumes);
            }
            throw new UnsupportedTypeException(URI, method, mediaType, supports);
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
        for (RequestDeserializer deserializer : deserializers) {
            String contentType = deserializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            map.put(mediaType, deserializer);
        }
    }

}
