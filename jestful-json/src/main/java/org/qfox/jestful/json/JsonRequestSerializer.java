package org.qfox.jestful.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Disposition;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Multihead;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.io.MultipartOutputStream;

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
	private final String contentType = "application/json";

	public String getContentType() {
		return contentType;
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		return bodies.size() == 0 || bodies.size() == 1 && supports(bodies.get(0));
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() == null || canSerialize(parameter.getValue().getClass());
	}

	public void serialize(Action action, String charset, OutputStream out) throws IOException {
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		for (Parameter parameter : parameters) {
			OutputStreamWriter osw = null;
			try {
				action.getRequest().setContentType(contentType + ";charset=" + charset);
				osw = new OutputStreamWriter(out, charset);
				writeValue(osw, parameter.getValue());
				break;
			} finally {
				IOKit.close(osw);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			Disposition disposition = new Disposition("form-data", parameter.getName());
			MediaType type = MediaType.valueOf(contentType + ";charset=" + charset);
			Multihead multihead = new Multihead(disposition, type);
			out.setNextMultihead(multihead);
			osw = new OutputStreamWriter(out, charset);
			writeValue(osw, parameter.getValue());
		} finally {
			IOKit.close(osw);
		}
	}

}
