package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.NoClosePrintWriter;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;

import java.io.OutputStream;
import java.io.Writer;
import java.util.*;

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
public class ResultRenderer implements Actor, Initialable, Configurable {
    private final Map<MediaType, ResponseSerializer> serializers = new HashMap<MediaType, ResponseSerializer>();
    private final List<Renderer> renderers = new ArrayList<Renderer>();

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Renderer renderer : renderers) {
            if (renderer instanceof Configurable) {
                ((Configurable) renderer).config(arguments);
            }
        }
    }

    public Object react(Action action) throws Exception {
        Restful restful = action.getRestful();
        Result result = action.getResult();
        Request request = action.getRequest();
        Response response = action.getResponse();
        // 忽略没有回应体和声明void返回值的方法
        if (restful.isReturnBody() == false || result.getKlass() == Void.TYPE) {
            return action.execute();
        }

        Object value = action.execute();

        if (result.isRendered() || response.isCommitted()) {
            return value;
        }

        for (Renderer renderer : renderers) {
            if (renderer.supports(action, value)) {
                renderer.render(action, value, request, response);
                result.setRendered(true);
                return value;
            }
        }

        switch (action.getDispatcher()) {
            case INCLUDE: {
                MediaType mediaType = (MediaType) action.getExtra().get(MediaType.class);
                if (mediaType != null) {
                    ResponseSerializer serializer = serializers.get(mediaType);
                    Writer writer = new NoClosePrintWriter(response.getResponseWriter(), true);
                    serializer.serialize(action, mediaType, writer);
                    writer.flush();
                } else {
                    String accept = request.getRequestHeader("Accept");
                    Accepts accepts = accept == null || accept.length() == 0 ? new Accepts(serializers.keySet()) : Accepts.valueOf(accept);
                    Accepts produces = action.getProduces();
                    Accepts supports = new Accepts(serializers.keySet());
                    String URI = action.getURI();
                    String method = action.getRestful().getMethod();
                    throw new NotAcceptableStatusException(URI, method, accepts, produces.isEmpty() ? supports : produces);
                }
            }
            break;
            default: {
                String charset = response.getResponseHeader("Content-Charset");
                String contentType = response.getContentType();
                if (contentType != null) {
                    MediaType mediaType = MediaType.valueOf(contentType);
                    ResponseSerializer serializer = serializers.get(mediaType);
                    OutputStream out = response.getResponseOutputStream();
                    serializer.serialize(action, mediaType, charset, out);
                    out.flush();
                } else {
                    String accept = request.getRequestHeader("Accept");
                    Accepts accepts = accept == null || accept.length() == 0 ? new Accepts(serializers.keySet()) : Accepts.valueOf(accept);
                    Accepts produces = action.getProduces();
                    Accepts supports = new Accepts(serializers.keySet());
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
            this.serializers.put(mediaType, serializer);
        }

        Collection<Renderer> renderers = beanContainer.find(Renderer.class).values();
        this.renderers.addAll(renderers);
    }

}
