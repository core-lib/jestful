package org.qfox.jestful.client.wrapper;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.codec.ResponseDecoder;

import java.util.Collection;
import java.util.LinkedHashMap;
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
 * @date 2016年5月15日 上午10:12:06
 * @since 1.0.0
 */
public class ResponseDecodeWrapper implements Actor, Initialable {
    private final Map<Encoding, ResponseDecoder> map = new LinkedHashMap<Encoding, ResponseDecoder>();

    public Object react(Action action) throws Exception {
        if (action.isAcceptEncode()) {
            Request request = action.getRequest();
            String acceptEncoding = request.getRequestHeader("Accept-Encoding");
            Encodings encodings = Encodings.valueOf(acceptEncoding);
            Map<Encoding, ResponseDecoder> decoders = new LinkedHashMap<Encoding, ResponseDecoder>();
            for (Encoding encoding : encodings) {
                decoders.put(encoding, map.get(encoding));
            }
            Response source = action.getResponse();
            Response target = new DecodedResponse(source, decoders);
            action.setResponse(target);
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<ResponseDecoder> decoders = beanContainer.find(ResponseDecoder.class).values();
        for (ResponseDecoder decoder : decoders) {
            String contentEncoding = decoder.getContentEncoding();
            Encoding encoding = Encoding.valueOf(contentEncoding);
            map.put(encoding, decoder);
        }
    }

}
