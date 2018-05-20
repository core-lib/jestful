package org.qfox.jestful.client;

import java.io.IOException;
import java.io.OutputStream;

/**
 * multipart/form-data 的部分
 */
public interface Part {

    /**
     * 内容类型
     *
     * @return 内容类型
     */
    String getContentType();

    /**
     * 写到输出流里面
     *
     * @param out 输出流
     * @throws IOException IO异常
     */
    void writeTo(OutputStream out) throws IOException;

}
