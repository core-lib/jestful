package org.qfox.jestful.core;

import java.io.*;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 下午8:54:46
 * @since 1.0.0
 */
public interface Request extends Closeable {

    String[] getHeaderKeys();

    String getRequestHeader(String name);

    void setRequestHeader(String name, String value);

    String[] getRequestHeaders(String name);

    void setRequestHeaders(String name, String[] values);

    int getConnTimeout();

    void setConnTimeout(int timeout);

    int getReadTimeout();

    void setReadTimeout(int timeout);

    void connect() throws IOException;

    String getContentType();

    void setContentType(String type);

    String getCharacterEncoding();

    void setCharacterEncoding(String env) throws UnsupportedEncodingException;

    InputStream getRequestInputStream() throws IOException;

    OutputStream getRequestOutputStream() throws IOException;

}
