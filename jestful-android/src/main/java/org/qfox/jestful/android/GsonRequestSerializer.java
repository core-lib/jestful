package org.qfox.jestful.android;

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
import org.qfox.jestful.core.io.IOUtils;
import org.qfox.jestful.core.io.MultipartOutputStream;

import com.google.gson.Gson;

public class GsonRequestSerializer implements RequestSerializer {
	private final String contentType = "application/json";
	private final Gson gson = new Gson();

	public String getContentType() {
		return contentType;
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		return bodies.size() == 0 ? true : bodies.size() == 1 ? supports(bodies.get(0)) : false;
	}

	public boolean supports(Parameter parameter) {
		return true;
	}

	public void serialize(Action action, String charset, OutputStream out) throws IOException {
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		for (Parameter parameter : parameters) {
			OutputStreamWriter osw = null;
			try {
				action.getRequest().setContentType(contentType + ";charset=" + charset);
				osw = new OutputStreamWriter(out, charset);
				gson.toJson(parameter.getValue(), osw);
				break;
			} finally {
				IOUtils.close(osw);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			Disposition disposition = Disposition.valueOf("form-data; name=\"" + parameter.getName() + "\"");
			MediaType type = MediaType.valueOf(contentType + ";charset=" + charset);
			Multihead multihead = new Multihead(disposition, type);
			out.setNextMultihead(multihead);
			osw = new OutputStreamWriter(out, charset);
			gson.toJson(parameter.getValue(), osw);
		} finally {
			IOUtils.close(osw);
		}
	}

}
