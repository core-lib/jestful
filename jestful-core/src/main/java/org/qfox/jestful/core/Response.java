package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
 * @date 2016年4月12日 下午8:55:03
 *
 * @since 1.0.0
 */
public interface Response {

	String[] getHeaderKeys();

	String getResponseHeader(String name);

	void setResponseHeader(String name, String value);

	String[] getResponseHeaders(String name);

	void setResponseHeaders(String name, String[] values);

	InputStream getResponseInputStream() throws IOException;

	OutputStream getResponseOutputStream() throws IOException;

	Status getResponseStatus() throws IOException;

	void setResponseStatus(Status status) throws IOException;

}
