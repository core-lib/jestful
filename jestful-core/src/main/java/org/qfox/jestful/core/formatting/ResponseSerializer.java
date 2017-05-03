package org.qfox.jestful.core.formatting;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

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
 * @date 2016年4月8日 下午5:08:58
 * @since 1.0.0
 */
public interface ResponseSerializer extends Formatting {

    void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException;

    void serialize(Action action, MediaType mediaType, Writer writer) throws IOException;

    void serialize(Object result, MediaType mediaType, String charset, OutputStream out) throws IOException;

}
