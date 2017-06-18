package org.qfox.jestful.core.io;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Multihead;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Description: multipart/form-data stream reader
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月9日 下午6:54:44
 * @since 1.0.0
 */
public class MultipartInputStream extends InputStream implements IOConstants {
    private final InputStream inputStream;
    private final String boundary;
    private final byte[] buffer;
    private int length = 0;
    private boolean end;
    private boolean start;

    public MultipartInputStream(InputStream inputStream, String boundary) throws IOException {
        super();
        if (inputStream == null || boundary == null) {
            throw new NullPointerException();
        }
        this.inputStream = inputStream;
        this.boundary = "--" + boundary;
        this.buffer = new byte[boundary.length() + 5];
        String line = IOKit.readln(inputStream);
        // 如果后面跟换行那么证明还有更多数据
        if (line.equals(this.boundary)) {
            start = false;
        }
        // 如果跟的是 "--" 证明已经读完了
        else if (line.equals(this.boundary + "--")) {
            end = true;
            start = false;
        } else {
            throw new IOException("wrong format of stream content");
        }
    }

    /**
     * get a multipart/form-data item from the source input stream, while it has already reach the end of
     * multipart/form-data item, return null. so use null return value to identify whether it has reach the end of
     * source input stream, the important thing you should know is that if current multipart/form-data item's content
     * didn't read fully, this method will ignore all remain content and go to the start of next multipart/form-data
     * item with return the next item header or end of source input stream with return null.
     *
     * @return next multipart/form-data item description
     * @throws IOException when IO error occur
     */
    public Multihead getNextMultihead() throws IOException {
        if (end) {
            return null;
        }
        while (end == false && start == true) {
            read();
        }
        if (end) {
            return null;
        }
        Multihead multihead = new Multihead(inputStream);
        start = true;
        return multihead;
    }

    @Override
    public int read() throws IOException {
        if (end == true || start == false) {
            return EOF;
        }
        int b;
        if (length > 0) {
            b = buffer[0] & 0xff;
            length--;
            // 前移
            for (int i = 0; i < length; i++) buffer[i] = buffer[i + 1];
        } else {
            b = inputStream.read();
            if (b == EOF) end = true;
        }

        if (b == '\r') {
            int len = length + inputStream.read(buffer, length, buffer.length - length);
            String line = "\r" + new String(buffer, 0, len);
            // 如果后面跟换行那么证明还有更多数据
            if (len == buffer.length && line.equals("\r\n" + boundary + "\r\n")) {
                start = false;
                length = 0;
                return EOF;
            }
            // 如果跟的是 "--" 证明已经读完了
            else if (len == buffer.length && line.equals("\r\n" + boundary + "--")) {
                end = true;
                length = 0;
                return EOF;
            }
            // 都不是那就是数据本身带有'\r'
            else {
                length = len;
                return b;
            }
        }
        return b;
    }

    /**
     * close softly
     */
    @Override
    public void close() throws IOException {
        close(false);
    }

    /**
     * close softly (if force == false or already reach the end of source input stream) <br/>
     * close forcibly (if force == true ignoring whether it has already reach the end of source input stream)
     *
     * @param force false: softly true: forcibly
     * @throws IOException when IO error occur
     */
    public void close(boolean force) throws IOException {
        if (end || force) {
            super.close();
            inputStream.close();
        }
    }

}
