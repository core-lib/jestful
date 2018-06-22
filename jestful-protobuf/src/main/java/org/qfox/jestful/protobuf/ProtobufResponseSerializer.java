package org.qfox.jestful.protobuf;

import com.google.protobuf.AbstractMessage;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.formatting.ResponseSerializer;

import java.io.IOException;
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
public class ProtobufResponseSerializer implements ResponseSerializer {

    public String getContentType() {
        return "application/protobuf";
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
        AbstractMessage message = (AbstractMessage) result;
        if (message != null) message.writeTo(out);
    }

}
