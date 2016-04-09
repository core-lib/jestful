package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.RequestDeserializer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
 * @date 2016年4月9日 下午6:28:28
 *
 * @since 1.0.0
 */
public class MultipartRequestDeserializer implements RequestDeserializer, ApplicationContextAware {
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

	public String getContentType() {
		return "multipart/form-data";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = in.read(buffer)) != -1) {
			System.out.write(buffer, 0, length);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<RequestDeserializer> deserializers = applicationContext.getBeansOfType(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

}
