package org.qfox.jestful.server.render;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.commons.IOUtils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月8日 下午12:30:55
 * @since 1.0.0
 */
public class TextResultRender implements Actor {
    private final Pattern pattern = Pattern.compile("@(\\(([^()]+?)\\))?\\:(.*)");

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
        if (text.matches(pattern.pattern())) {
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            String type = matcher.group(2) != null ? matcher.group(2) : "text/plain";
            String content = matcher.group(3) != null ? matcher.group(3) : "";
            try {
                Response response = action.getResponse();
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
                        IOUtils.close(writer);
                    }
                    break;
                }
            } catch (Exception e) {
                throw e;
            } finally {
                result.setRendered(true);
            }
        }
        return value;
    }

}
