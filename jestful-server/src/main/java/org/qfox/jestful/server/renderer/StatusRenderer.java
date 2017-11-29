package org.qfox.jestful.server.renderer;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.MapKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.http.HttpStatus;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Payne on 2017/4/27.
 */
public class StatusRenderer implements Renderer {
    private Pattern pattern = Pattern.compile("^@status:[ ]?(\\d{3})(?:[ ]([^{}]*))?(?:[ ]\\{([^{}]*)})?(?:[ ]([\\s\\S]*))?$");

    @Override
    public boolean supports(Action action, Object value) {
        return value instanceof String && ((String) value).matches(pattern.pattern());
    }

    @Override
    public void render(Action action, Object value, Request request, Response response) throws Exception {
        String text = (String) value;
        Matcher matcher = pattern.matcher(text);
        matcher.find();

        Integer code = Integer.valueOf(matcher.group(1));
        String message = matcher.group(2);
        String header = matcher.group(3);
        String body = matcher.group(4);

        switch (action.getDispatcher()) {
            case INCLUDE: {
                Writer writer = response.getResponseWriter();
                writer.write(body != null ? body : "");
                writer.flush();
            }
            break;
            default: {
                String charset = response.getResponseHeader("Content-Charset");
                response.setContentType("text/html; charset=" + charset);
                Map<String, String> headers = header != null ? MapKit.valueOf(header) : new LinkedHashMap<String, String>();
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    response.setResponseHeader(entry.getKey(), entry.getValue());
                }
                response.setResponseStatus(new HttpStatus(code, message));

                OutputStream out = response.getResponseOutputStream();
                Writer writer = new OutputStreamWriter(out, charset);
                writer.write(body != null ? body : "");
                writer.flush();
                IOKit.close(writer);
            }
            break;
        }
    }

}
