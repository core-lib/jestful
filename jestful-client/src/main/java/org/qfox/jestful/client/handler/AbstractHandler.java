package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 基本处理器
 *
 * @author Payne 646742615@qq.com
 * 2019/9/29 16:22
 */
public abstract class AbstractHandler implements Handler {

    @Override
    public void send(Client client, Action action) throws Exception {
        Request request = action.getRequest();
        Restful restful = action.getRestful();
        if (restful.isAcceptBody()) {
            client.serialize(action);
        } else {
            request.connect();
        }
    }

    @Override
    public void receive(Client client, Action action) throws Exception {
        Response response = action.getResponse();
        if (!response.isResponseSuccess()) {
            String contentType = response.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            String charset = mediaType.getCharset();
            if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
            if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
            if (StringKit.isBlank(charset)) charset = Charset.defaultCharset().name();
            Status status = response.getResponseStatus();
            Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
            String[] keys = response.getHeaderKeys();
            for (String key : keys) header.put(key == null ? "" : key, response.getResponseHeaders(key));
            InputStream in = response.getResponseInputStream();
            InputStreamReader reader = in == null ? null : new InputStreamReader(in, charset);
            String body = reader != null ? IOKit.toString(reader) : null;
            throw new UnexpectedStatusException(action.getRequestURI(), action.getRestful().getMethod(), status, header, body);
        }

        Restful restful = action.getRestful();
        if (restful.isReturnBody()) {
            client.deserialize(action);
        }
    }

}
