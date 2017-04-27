package org.qfox.jestful.server.renderer;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Payne on 2017/4/27.
 */
public class TextRenderer implements Renderer {
    private final Pattern pattern = Pattern.compile("@(\\(([^()]+?)\\))?:([\\s\\S]*)");

    @Override
    public boolean supports(Action action, Object value) {
        return value instanceof String && ((String) value).matches(pattern.pattern());
    }

    @Override
    public void render(Action action, Object value, Request request, Response response) throws Exception {
        String text = (String) value;
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        String type = matcher.group(2) != null ? matcher.group(2) : "text/plain";
        String content = matcher.group(3) != null ? matcher.group(3) : "";
        switch (action.getDispatcher()) {
            case INCLUDE: {
                Writer writer = response.getResponseWriter();
                writer.write(content);
                writer.flush();
            }
            break;
            default: {
                String charset = response.getResponseHeader("Content-Charset");
                response.setContentType(type + "; charset=" + charset);
                OutputStream out = response.getResponseOutputStream();
                Writer writer = new OutputStreamWriter(out, charset);
                writer.write(content);
                writer.flush();
                IOKit.close(writer);
            }
            break;
        }
    }
}
