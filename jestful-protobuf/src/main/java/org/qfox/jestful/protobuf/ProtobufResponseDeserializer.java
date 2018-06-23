package org.qfox.jestful.protobuf;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Body;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
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
 * @date 2016年4月15日 下午5:12:19
 * @since 1.0.0
 */
public class ProtobufResponseDeserializer implements ResponseDeserializer {

    public String getContentType() {
        return "application/protobuf";
    }

    public void setDeserializationDateFormat(DateFormat dateFormat) {

    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        try {
            Result result = action.getResult();
            Body body = result.getBody();
            Class<?> klass = (Class<?>) body.getType();
            Method method = klass.getMethod("parseFrom", InputStream.class);
            Object value = method.invoke(null, in);
            body.setValue(value);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException {
        throw new UnsupportedOperationException();
    }

}
