package org.qfox.jestful.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestDeserializer;

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
 * @date 2016年4月11日 下午9:39:11
 *
 * @since 1.0.0
 */
public class JavaRequestDeserializer implements RequestDeserializer {

	public String getContentType() {
		return "application/x-java-serialized-object";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		Parameters parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.BODY) {
				continue;
			}
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(in);
				Object value = ois.readObject();
				parameter.setValue(value);
				break;
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			} finally {
				IOUtils.close(ois);
			}
		}
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, InputStream in) throws IOException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			Object value = ois.readObject();
			parameter.setValue(value);
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			IOUtils.close(ois);
		}
	}

}
