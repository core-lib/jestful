package org.qfox.jestful.core.formatting;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;

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
 * @date 2016年4月8日 下午5:21:07
 * @since 1.0.0
 */
public interface ResponseDeserializer extends Formatting {

    void setDeserializationDateFormat(DateFormat dateFormat);

    void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException;

    void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException;

}
