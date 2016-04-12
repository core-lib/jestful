package org.qfox.jestful.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.RequestSerializer;
import org.qfox.jestful.core.annotation.Variable.Position;

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
 * @date 2016年4月12日 下午6:50:18
 *
 * @since 1.0.0
 */
public class JsonRequestSerializer extends ObjectMapper implements RequestSerializer {
	private static final long serialVersionUID = -240520430267871287L;

	public String getContentType() {
		return "application/json";
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() != null ? canSerialize(parameter.getValue().getClass()) : true;
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(out, mediaType.getCharset());
				writeValue(osw, parameter.getValue());
				break;
			} finally {
				IOUtils.close(osw);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, Multipart multipart, OutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			MediaType mediaType = multipart.getType();
			osw = new OutputStreamWriter(out, mediaType.getCharset());
			writeValue(osw, parameter.getValue());
		} finally {
			IOUtils.close(osw);
		}
	}

}
