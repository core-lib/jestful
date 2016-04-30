package org.qfox.jestful.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestDeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月12日 下午5:48:00
 *
 * @since 1.0.0
 */
public class JsonRequestDeserializer extends ObjectMapper implements RequestDeserializer {
	private static final long serialVersionUID = 5796735653228955284L;

	public String getContentType() {
		return "application/json";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		Parameters parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(in, mediaType.getCharset());
				Type type = parameter.getType();
				Object value = readValue(isr, constructType(type));
				parameter.setValue(value);
				break;
			} finally {
				IOUtils.close(isr);
			}
		}
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, InputStream in) throws IOException {
		InputStreamReader isr = null;
		try {
			MediaType mediaType = multihead.getType();
			isr = new InputStreamReader(in, mediaType.getCharset());
			Type type = parameter.getType();
			Object value = readValue(isr, constructType(type));
			parameter.setValue(value);
		} finally {
			IOUtils.close(isr);
		}
	}

}
