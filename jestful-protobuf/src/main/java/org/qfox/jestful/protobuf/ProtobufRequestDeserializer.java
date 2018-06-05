package org.qfox.jestful.protobuf;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

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
 * @date 2016年4月11日 下午9:39:11
 * @since 1.0.0
 */
public class ProtobufRequestDeserializer implements RequestDeserializer {

    public String getContentType() {
        return "application/protobuf";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY, true);
        for (Parameter parameter : parameters) {
            try {
                Method method = parameter.getKlass().getMethod("parseFrom", InputStream.class);
                Object value = method.invoke(null, in);
                parameter.setValue(value);
                break;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        try {
            Method method = parameter.getKlass().getMethod("parseFrom", InputStream.class);
            Object value = method.invoke(null, in);
            parameter.setValue(value);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
