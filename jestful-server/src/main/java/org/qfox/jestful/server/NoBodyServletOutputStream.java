package org.qfox.jestful.server;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * Created by yangchangpei on 16/11/29.
 */
public class NoBodyServletOutputStream extends ServletOutputStream {
    private int contentLength = 0;

    @Override
    public void write(byte[] b) throws IOException {
        contentLength += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        contentLength += len;
    }

    @Override
    public void write(int b) throws IOException {
        contentLength += 1;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

}
