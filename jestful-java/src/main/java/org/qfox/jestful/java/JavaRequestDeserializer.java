package org.qfox.jestful.java;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
public class JavaRequestDeserializer implements RequestDeserializer {

    public String getContentType() {
        return "application/x-java-serialized-object";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY, true);
        for (Parameter parameter : parameters) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(in);
                Object value = ois.readObject();
                parameter.setValue(value);
                break;
            } catch (ClassNotFoundException e) {
                throw new JestfulIOException(e);
            } finally {
                IOKit.close(ois);
            }
        }
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
            Object value = ois.readObject();
            parameter.setValue(value);
        } catch (ClassNotFoundException e) {
            throw new JestfulIOException(e);
        } finally {
            IOKit.close(ois);
        }
    }

}
