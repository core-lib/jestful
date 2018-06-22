package org.qfox.jestful.java;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.formatting.ResponseSerializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
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
 * @date 2016年4月9日 下午3:33:36
 * @since 1.0.0
 */
public class JavaResponseSerializer implements ResponseSerializer {

    public String getContentType() {
        return "application/x-java-serialized-object";
    }

    public void setSerializationDateFormat(DateFormat dateFormat) {

    }

    public void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException {
        serialize(action.getResult().getBody().getValue(), mediaType, charset, out);
    }

    @Override
    public void serialize(Action action, MediaType mediaType, Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void serialize(Object result, MediaType mediaType, String charset, OutputStream out) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(result);
        } finally {
            IOKit.close(oos);
        }
    }

}
