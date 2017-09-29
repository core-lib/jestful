package org.qfox.jestful.server.wrapper;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.codec.ResponseEncoder;
import org.qfox.jestful.core.exception.NoSuchCodecException;
import org.qfox.jestful.server.JestfulServletResponse;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseEncodeWrapper implements Actor, Initialable {
    private final Map<Encoding, ResponseEncoder> map = new LinkedHashMap<Encoding, ResponseEncoder>();

    public Object react(Action action) throws Exception {
        if (action.isAllowEncode()) {
            Encodings encodings = action.getContentEncodings().clone();
            Encodings availables = new Encodings(map.keySet());
            if (!encodings.isEmpty()) {
                encodings.retainAll(availables);
            } else {
                encodings = availables;
            }
            if (encodings.isEmpty()) {
                Encodings expects = action.getContentEncodings().clone();
                Encodings actuals = availables;
                throw new NoSuchCodecException(expects, actuals);
            }
            Request request = action.getRequest();
            String acceptEncoding = request.getRequestHeader("Accept-Encoding");
            if (acceptEncoding == null || acceptEncoding.length() == 0) {
                return action.execute();
            }
            Encodings remains = encodings.clone();
            Encodings accepts = Encodings.valueOf(acceptEncoding);
            remains.retainAll(accepts);
            if (remains.isEmpty()) {
                return action.execute();
            }
            Encoding encoding = remains.first();
            ResponseEncoder encoder = map.get(encoding);
            Response source = action.getResponse();
            Response target = new EncodedServletResponse((JestfulServletResponse) source, encoding, encoder);
            action.setResponse(target);
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<ResponseEncoder> encoders = beanContainer.find(ResponseEncoder.class).values();
        for (ResponseEncoder encoder : encoders) {
            String contentEncoding = encoder.getContentEncoding();
            Encoding encoding = Encoding.valueOf(contentEncoding);
            map.put(encoding, encoder);
        }
    }

}
