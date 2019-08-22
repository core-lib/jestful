package org.qfox.jestful.cookie;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by payne on 2017/3/26.
 */
public class CookieManagedRequest extends RequestWrapper {
    private final URI uri;
    private final CookieHandler cookieHandler;

    public CookieManagedRequest(Request request, URI uri, CookieHandler cookieHandler) {
        super(request);
        this.uri = uri;
        this.cookieHandler = cookieHandler;
    }

    @Override
    public void connect() throws IOException {
        Map<String, List<String>> header = new LinkedHashMap<String, List<String>>();
        String[] keys = getHeaderKeys();
        for (String key : keys) {
            String[] values = getRequestHeaders(key);
            header.put(key, Arrays.asList(values));
        }
        Map<String, List<String>> map = cookieHandler.get(uri, header);
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            setRequestHeaders(entry.getKey(), entry.getValue().toArray(new String[0]));
        }
        super.connect();
    }
}
