package org.qfox.jestful.server.render;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.NoClosePrintWriter;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月9日 下午3:46:30
 * @since 1.0.0
 */
public class DefaultResultRender implements Actor, Initialable {
    private final Map<MediaType, ResponseSerializer> map = new HashMap<MediaType, ResponseSerializer>();

    public Object react(Action action) throws Exception {
        Restful restful = action.getRestful();
        Result result = action.getResult();
        // 忽略没有回应体和声明void返回值的方法
        if (restful.isReturnBody() == false || result.getKlass() == Void.TYPE) {
            return action.execute();
        }

        Object value = action.execute();

        if (result.isRendered()) {
            return value;
        }

        switch (action.getDispatcher()) {
            case INCLUDE: {
                Response response = action.getResponse();
                MediaType mediaType = (MediaType) action.getExtra().get(MediaType.class);
                ResponseSerializer serializer = map.get(mediaType);
                if (serializer != null) {
                    Writer writer = new NoClosePrintWriter(response.getResponseWriter(), true);
                    serializer.serialize(action, mediaType, writer);
                    writer.flush();
                } else {
                    Request request = action.getRequest();
                    String accept = request.getRequestHeader("Accept");
                    Accepts accepts = accept == null || accept.length() == 0 ? new Accepts(map.keySet()) : Accepts.valueOf(accept);
                    Accepts produces = action.getProduces();
                    Accepts supports = new Accepts(map.keySet());
                    String URI = action.getURI();
                    String method = action.getRestful().getMethod();
                    throw new NotAcceptableStatusException(URI, method, accepts, produces.isEmpty() ? supports : produces);
                }
            }
            break;
            default: {
                Response response = action.getResponse();
                String charset = response.getResponseHeader("Content-Charset");
                String contentType = response.getContentType();
                MediaType mediaType = MediaType.valueOf(contentType);
                ResponseSerializer serializer = map.get(mediaType);
                if (serializer != null) {
                    OutputStream out = response.getResponseOutputStream();
                    serializer.serialize(action, mediaType, charset, out);
                    out.flush();
                } else {
                    Request request = action.getRequest();
                    String accept = request.getRequestHeader("Accept");
                    Accepts accepts = accept == null || accept.length() == 0 ? new Accepts(map.keySet()) : Accepts.valueOf(accept);
                    Accepts produces = action.getProduces();
                    Accepts supports = new Accepts(map.keySet());
                    String URI = action.getURI();
                    String method = action.getRestful().getMethod();
                    throw new NotAcceptableStatusException(URI, method, accepts, produces.isEmpty() ? supports : produces);
                }
            }
            break;
        }

        result.setRendered(true);

        return value;
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<ResponseSerializer> serializers = beanContainer.find(ResponseSerializer.class).values();
        for (ResponseSerializer serializer : serializers) {
            String contentType = serializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            map.put(mediaType, serializer);
        }
    }

}
