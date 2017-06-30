package org.qfox.jestful.server.wrapper;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.codec.RequestDecoder;
import org.qfox.jestful.core.exception.NoSuchCodecException;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.exception.DisallowEncodeException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestDecodeWrapper implements Actor, Initialable {
    private final Map<Encoding, RequestDecoder> map = new LinkedHashMap<Encoding, RequestDecoder>();

    public Object react(Action action) throws Exception {
        Request source = action.getRequest();
        String contentEncoding = source.getRequestHeader("Content-Encoding");
        if (contentEncoding == null || contentEncoding.length() == 0) {
            return action.execute();
        }
        if (action.isAcceptEncode()) {
            Encodings encodings = action.getAcceptEncodings().clone();
            Encodings availables = new Encodings(map.keySet());
            if (encodings.isEmpty() == false) {
                encodings.retainAll(availables);
            } else {
                encodings = availables;
            }
            Encoding encoding = Encoding.valueOf(contentEncoding);
            if (encodings.contains(encoding) == false) {
                Encodings expects = Encodings.valueOf(contentEncoding);
                Encodings actuals = encodings;
                throw new NoSuchCodecException(expects, actuals);
            }
            Request target = new DecodedServletRequest((JestfulServletRequest) source, encoding, map.get(encoding));
            action.setRequest(target);
        } else {
            String message = "current instance is disallow request with Content-Encoding perhaps cause you set acceptEncode=false in jestful servlet/filter init parameter";
            throw new DisallowEncodeException(message);
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<RequestDecoder> decoders = beanContainer.find(RequestDecoder.class).values();
        for (RequestDecoder decoder : decoders) {
            String contentEncoding = decoder.getContentEncoding();
            Encoding encoding = Encoding.valueOf(contentEncoding);
            map.put(encoding, decoder);
        }
    }

}
