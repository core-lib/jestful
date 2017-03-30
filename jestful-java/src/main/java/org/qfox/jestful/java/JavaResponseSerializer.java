package org.qfox.jestful.java;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.commons.IOKit;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
 * @date 2016年4月9日 下午3:33:36
 * @since 1.0.0
 */
public class JavaResponseSerializer implements ResponseSerializer {

    public String getContentType() {
        return "application/x-java-serialized-object";
    }

    public void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(action.getResult().getValue());
        } finally {
            IOKit.close(oos);
        }
    }

    @Override
    public void serialize(Action action, MediaType mediaType, Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }

}
