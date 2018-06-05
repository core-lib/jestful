package org.qfox.jestful.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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
 * @date 2016年4月12日 下午5:48:00
 * @since 1.0.0
 */
public class JsonRequestDeserializer extends ObjectMapper implements RequestDeserializer {
    private static final long serialVersionUID = 5796735653228955284L;

    public JsonRequestDeserializer() {
        this.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        this.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        this.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        this.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
    }

    public String getContentType() {
        return "application/json";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY, true);
        for (Parameter parameter : parameters) {
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(in, charset);
                Type type = parameter.getType();
                Object value = readValue(isr, constructType(type));
                parameter.setValue(value);
                break;
            } finally {
                IOKit.close(isr);
            }
        }
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, charset);
            Type type = parameter.getType();
            Object value = readValue(isr, constructType(type));
            parameter.setValue(value);
        } finally {
            IOKit.close(isr);
        }
    }

}
