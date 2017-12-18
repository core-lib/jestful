package org.qfox.jestful.client.cache;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;
import org.qfox.jestful.core.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

class CachedResponse extends ResponseWrapper {
    private final Status status;
    private final Map<String, String[]> header;
    private final InputStream body;
    private volatile Reader reader;

    CachedResponse(Response response, Cache cache) {
        super(response);
        this.status = cache.getStatus();
        this.header = cache.getHeader();
        this.body = cache.getBody();
    }

    @Override
    public Status getResponseStatus() throws IOException {
        return status;
    }

    @Override
    public boolean isResponseSuccess() throws IOException {
        return getResponseStatus().isSuccess();
    }

    @Override
    public String[] getHeaderKeys() {
        return header.keySet().toArray(new String[header.size()]);
    }

    @Override
    public String getResponseHeader(String name) {
        return header.containsKey(name) ? header.get(name)[0] : null;
    }

    @Override
    public void setResponseHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getResponseHeaders(String name) {
        return header.get(name);
    }

    @Override
    public void setResponseHeaders(String name, String[] values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        return body;
    }

    @Override
    public Reader getResponseReader() throws IOException {
        if (reader != null) return reader;
        InputStream in = getResponseInputStream();
        String characterEncoding = getCharacterEncoding();
        return reader = characterEncoding != null ? new InputStreamReader(in, characterEncoding) : new InputStreamReader(in);
    }
}
