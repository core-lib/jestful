package org.qfox.jestful.server.mapper;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Request;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class HeadersMapper implements Actor {

    @Override
    public Object react(Action action) throws Exception {
        Map<String, String[]> headers = new CaseInsensitiveMap<String, String[]>();
        Request request = action.getRequest();
        String charset = action.getHeaderEncodeCharset();
        String[] keys = request.getHeaderKeys();
        for (String key : keys) {
            String[] values = request.getRequestHeaders(key);
            headers.put(URLDecoder.decode(key, charset), values);
        }
        action.setHeaders(headers);
        return action.execute();
    }

}
