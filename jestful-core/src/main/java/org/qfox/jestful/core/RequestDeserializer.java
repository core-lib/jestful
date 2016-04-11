package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;

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
 * @date 2016年4月8日 下午5:21:07
 *
 * @since 1.0.0
 */
public interface RequestDeserializer {

	String getContentType();

	void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException;

	void deserialize(Action action, Parameter parameter, Multipart multipart, InputStream in) throws IOException;

}
