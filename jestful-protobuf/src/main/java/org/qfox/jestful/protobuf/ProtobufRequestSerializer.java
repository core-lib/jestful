package org.qfox.jestful.protobuf;

import com.google.protobuf.AbstractMessage;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

import java.io.IOException;
import java.io.OutputStream;
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
 * @date 2016年4月15日 下午5:05:22
 * @since 1.0.0
 */
public class ProtobufRequestSerializer implements RequestSerializer {
    private final String contentType = "application/protobuf";

    public String getContentType() {
        return contentType;
    }

    public boolean supports(Action action) {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        return bodies.size() == 0 || bodies.size() == 1 && supports(bodies.get(0));
    }

    public boolean supports(Parameter parameter) {
        return parameter.getValue() == null || parameter.getValue() instanceof AbstractMessage;
    }

    public void serialize(Action action, String charset, OutputStream out) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        for (Parameter parameter : parameters) {
            action.getRequest().setContentType(contentType);
            AbstractMessage message = (AbstractMessage) parameter.getValue();
            if (message != null) message.writeTo(out);
            break;
        }
    }

    public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
        Disposition disposition = Disposition.valueOf("form-data; name=\"" + parameter.getName() + "\"");
        MediaType type = MediaType.valueOf(contentType);
        Multihead multihead = new Multihead(disposition, type);
        out.setNextMultihead(multihead);
        AbstractMessage message = (AbstractMessage) parameter.getValue();
        if (message != null) message.writeTo(out);
    }

}
