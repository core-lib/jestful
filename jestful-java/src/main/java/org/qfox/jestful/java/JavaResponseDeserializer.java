package org.qfox.jestful.java;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;

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
public class JavaResponseDeserializer implements ResponseDeserializer {

    public String getContentType() {
        return "application/x-java-serialized-object";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
            Result result = action.getResult();
            Object value = ois.readObject();
            result.getBody().setValue(value);
        } catch (ClassNotFoundException e) {
            throw new JestfulIOException(e);
        } finally {
            IOKit.close(ois);
        }
    }

    @Override
    public void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException {
        throw new UnsupportedOperationException();
    }

}
