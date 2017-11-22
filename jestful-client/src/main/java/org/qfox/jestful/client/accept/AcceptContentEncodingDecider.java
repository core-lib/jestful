package org.qfox.jestful.client.accept;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.codec.ResponseDecoder;
import org.qfox.jestful.core.exception.NoSuchCodecException;

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
 * @date 2016年5月14日 上午10:12:52
 * @since 1.0.0
 */
public class AcceptContentEncodingDecider implements Actor, Initialable {
    private final Map<Encoding, ResponseDecoder> map = new LinkedHashMap<Encoding, ResponseDecoder>();

    public Object react(Action action) throws Exception {
        if (action.isAcceptEncode()) {
            Encodings encodings = action.getAcceptEncodings().clone();
            Encodings availables = new Encodings(map.keySet());
            if (!encodings.isEmpty()) {
                encodings.retainAll(availables);
            } else {
                encodings = availables;
            }
            if (encodings.isEmpty()) {
                Encodings expects = action.getAcceptEncodings();
                Encodings actuals = availables;
                throw new NoSuchCodecException(expects, actuals);
            }
            Request request = action.getRequest();
            request.setRequestHeader("Accept-Encoding", encodings.toString());
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
