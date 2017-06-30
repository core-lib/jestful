package org.qfox.jestful.cookie;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;

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
public class CookieManagedResponse extends ResponseWrapper {
    private final URI uri;
    private final CookieHandler cookieHandler;

    public CookieManagedResponse(Response response, URI uri, CookieHandler cookieHandler) {
        super(response);
        this.uri = uri;
        this.cookieHandler = cookieHandler;
    }

    @Override
    public void close() throws IOException {
        Map<String, List<String>> header = new LinkedHashMap<String, List<String>>();
        String[] keys = getHeaderKeys();
        for (String key : keys) {
            String[] values = getResponseHeaders(key);
            header.put(key, Arrays.asList(values));
        }
        cookieHandler.put(uri, header);
        super.close();
    }
}
