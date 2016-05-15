package org.qfox.jestful.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

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
 * @date 2016年4月15日 下午5:12:19
 *
 * @since 1.0.0
 */
public class JavaResponseDeserializer implements ResponseDeserializer {

	public String getContentType() {
		return "application/x-java-serialized-object";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			Result result = action.getResult();
			Object value = ois.readObject();
			result.getBody().setValue(value);
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			IOUtils.close(ois);
		}
	}

}
