package org.qfox.jestful.java;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestSerializer;

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
 * @date 2016年4月15日 下午5:05:22
 *
 * @since 1.0.0
 */
public class JavaRequestSerializer implements RequestSerializer {

	public String getContentType() {
		return "application/x-java-serialized-object";
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() != null ? parameter.getValue() instanceof Serializable : true;
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(out);
				oos.writeObject(parameter.getValue());
				break;
			} finally {
				IOUtils.close(oos);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, Multihead multihead, OutputStream out) throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(out);
			oos.writeObject(parameter.getValue());
		} finally {
			IOUtils.close(oos);
		}
	}

}
