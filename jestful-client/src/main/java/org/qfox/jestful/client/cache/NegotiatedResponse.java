package org.qfox.jestful.client.cache;

import org.qfox.jestful.client.cache.impl.http.HttpCacheConstants;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.io.BackupInputStream;
import org.qfox.jestful.commons.io.NoneInputStream;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;

import java.io.*;

public class NegotiatedResponse extends ResponseWrapper implements HttpCacheConstants {
    private final ByteArrayOutputStream body = new ByteArrayOutputStream();
    private volatile Reader reader;

    NegotiatedResponse(Response response) {
        super(response);
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        InputStream in = super.getResponseInputStream();
        return new BackupInputStream(in != null ? in : new NoneInputStream(), body);
    }

    @Override
    public Reader getResponseReader() throws IOException {
        if (reader != null) return reader;
        InputStream in = getResponseInputStream();
        String characterEncoding = getCharacterEncoding();
        return reader = characterEncoding != null ? new InputStreamReader(in, characterEncoding) : new InputStreamReader(in);
    }

    public long getAgeResponded() {
        String age = getResponseHeader(AGE);
        return StringKit.isEmpty(age) ? 0L : Long.valueOf(age);
    }

    public InputStream getResponseBodyInputStream() {
        return new ByteArrayInputStream(body.toByteArray());
    }

}
