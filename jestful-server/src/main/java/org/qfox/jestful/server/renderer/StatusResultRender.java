package org.qfox.jestful.server.renderer;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.MapKit;
import org.qfox.jestful.core.*;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Payne on 2017/4/9.
 */
public class StatusResultRender implements Actor, Initialable {
    private String prefix = "status:";
    private Pattern pattern = null;

    @Override
    public void initialize(BeanContainer beanContainer) {
        this.pattern = Pattern.compile("^" + prefix + ("[ ]?(\\d{3})(?:[ ]([^{}]*))?(?:[ ]\\{([^{}]*)})?(?:[ ](.*))?") + "$");
    }

    @Override
    public Object react(Action action) throws Exception {
        Object value = action.execute();
        Result result = action.getResult();
        if (result.isRendered()) {
            return value;
        }

        if (value instanceof String == false) {
            return value;
        }
        String text = (String) value;

        Response response = action.getResponse();

        if (text.matches(pattern.pattern())) {
            result.setRendered(true);

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
                    Map<String, String> headers = header != null ? MapKit.valueOf(header) : new HashMap<String, String>();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        response.setResponseHeader(entry.getKey(), entry.getValue());
                    }
                    response.setResponseStatus(new Status(code, message));

                    OutputStream out = response.getResponseOutputStream();
                    Writer writer = new OutputStreamWriter(out, charset);
                    writer.write(body != null ? body : "");
                    writer.flush();
                    IOKit.close(writer);
                }
                break;
            }
        }

        return value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
