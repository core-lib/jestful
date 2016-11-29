package org.qfox.jestful.server.decider;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.NoSuchCharsetException;

import java.nio.charset.Charset;

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
 * @date 2016年6月8日 上午10:46:22
 * @since 1.0.0
 */
public class ContentCharsetDecider implements Actor {
    private final Charsets charsets = new Charsets(Charset.availableCharsets().keySet().toArray(new String[0]));

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        Response response = action.getResponse();
        String charset = response.getCharacterEncoding();
        if (charset == null) {
            String accept = request.getRequestHeader("Accept-Charset");
            Charsets accepts = accept == null || accept.length() == 0 ? charsets.clone() : Charsets.valueOf(accept);
            Charsets options = action.getContentCharsets().clone();
            Charsets supports = charsets.clone();
            if ((accept == null || accept.length() == 0) && options.isEmpty()) {
                charset = Charset.defaultCharset().name();
            } else if (options.isEmpty()) {
                accepts.retainAll(supports);
                if (accepts.isEmpty()) {
                    throw new NoSuchCharsetException(Charsets.valueOf(accept), supports);
                }
                charset = accepts.first().getName();
            } else {
                options.retainAll(supports);
                if (options.isEmpty()) {
                    throw new NoSuchCharsetException(action.getContentCharsets().clone(), supports);
                }
                charset = options.first().getName();
            }
        }
        response.setResponseHeader("Content-Charset", charset);
        action.getExtra().put(Charset.class, Charset.forName(charset));
        return action.execute();
    }

}
