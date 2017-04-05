package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.JestfulClientResponse;
import org.qfox.jestful.client.aio.AioByteArrayOutputStream;
import org.qfox.jestful.client.aio.AioResponse;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Status;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/3/25.
 */
public class JestfulAioHttpClientResponse extends JestfulClientResponse implements AioResponse {
    private AioByteArrayOutputStream head = new AioByteArrayOutputStream();
    private AioByteArrayOutputStream body = new AioByteArrayOutputStream();

    private Boolean chunked;
    private int crlfs;
    private int total;
    private int position;
    private ByteBuffer last;

    private Status status = new Status(200, "OK");
    private InputStream in;
    private Reader reader;
    private final Object lock = new Object();
    private boolean closed;

    protected JestfulAioHttpClientResponse(Action action, Connector connector, Gateway gateway) {
        super(action, connector, gateway);
    }

    private void doReadHeader() throws IOException {
        InputStream in = head.toInputStream();
        String head = IOKit.readln(in);
        status = new Status(head);

        while (in.available() > 0) {
            String line = IOKit.readln(in);
            int index = line != null ? line.indexOf(':') : -1;
            if (index == -1) continue;
            String key = line.substring(0, index).trim();
            String value = line.substring(index + 1).trim();
            setResponseHeader(key, value);
        }

        String transferEncoding = getResponseHeader("Transfer-Encoding");
        if (transferEncoding != null && transferEncoding.trim().toLowerCase().equals("chunked")) {
            chunked = true;
            last = ByteBuffer.wrap(new byte[]{'\r', '\n'});
            total = 0;
            position = 0;
        } else {
            chunked = false;
            String contentLength = getResponseHeader("Content-Length");
            total = contentLength != null && contentLength.matches("\\d+") ? Integer.valueOf(contentLength) : 0;
            position = 0;
        }
    }

    @Override
    public boolean load(ByteBuffer buffer) throws IOException {
        return doLoad(buffer);
    }

    private boolean doLoad(ByteBuffer buffer) throws IOException {
        if (buffer.remaining() == 0) {
            return false;
        }

        // 协议头还没读完
        if (chunked == null) {
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                head.write(b);
                switch (b) {
                    case '\n':
                        if (++crlfs == 2) {
                            doReadHeader();
                            crlfs = 0;
                            return doLoad(buffer);
                        }
                        break;
                    case '\r':
                        break;
                    default:
                        crlfs = 0;
                        break;
                }
            }
        }
        // Transfer-Encoding == chunked
        else if (chunked) {
            // 上一段已读完 或者刚开始读回应体
            if (position == total) {
                // 读一行 注意这里有可能被NIO截断 需要等待下一次或多次 读取才能保证读到一行
                AioByteArrayOutputStream cache = new AioByteArrayOutputStream();
                // 把上次留下来的加到最开始位置
                while (last.hasRemaining()) {
                    byte b = last.get();
                    cache.write(b);
                    if (b == '\n') {
                        crlfs++;
                    }
                }
                // 再往后面追加
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    cache.write(b);
                    // 利用短路原理 b != '\n' 时 不会执行 ++crlfs !!!
                    if (b == '\n' && ++crlfs == 2) {
                        // 开始读取chunk size
                        InputStream in = cache.toInputStream();
                        // 去掉一个空行
                        IOKit.readln(in);
                        // 紧跟着的一行就是chunk size
                        String hex = IOKit.readln(in);
                        total = hex != null ? Integer.valueOf(hex, 16) : 0;
                        position = 0;
                        crlfs = 0;
                        if (total == 0) {
                            return true;
                        }
                        // 递归读取段内容
                        return doLoad(buffer);
                    }
                }
                // 来到这里证明真的在 chunk size 的位置被截断了 那么保留下来 等待下次接收的时候再确定 chunk size
                crlfs = 0;
                last = cache.toByteBuffer();
                return false;
            }
            // 已经确定了该段的长度 现在读取该段的内容
            else {
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    body.write(b);
                    // 该段读完 递归下一段
                    if (++position == total) {
                        // 这里会带来一个问题 因为把一个没用的换行符留给到下次程序读chunk size的时候 但是这里又没法保证能读出来这个换行符 因为有可能截断了!!!!
                        return doLoad(buffer);
                    }
                }
                // 来到这里证明该段还没读完 等待下一次接收吧
                return false;
            }
        }
        // Transfer-Encoding != chunked
        else {
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                body.write(b);
                if (++position == total) {
                    return true;
                }
            }
        }

        return chunked != null && position == total;
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        if (in != null) {
            return in;
        }
        synchronized (lock) {
            if (in != null) {
                return in;
            }
            return in = body.toInputStream();
        }
    }

    @Override
    public OutputStream getResponseOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader getResponseReader() throws IOException {
        if (reader != null) {
            return reader;
        }
        synchronized (lock) {
            if (reader != null) {
                return reader;
            }
            return reader = characterEncoding != null ? new InputStreamReader(getResponseInputStream(), characterEncoding) : new InputStreamReader(getResponseInputStream());
        }
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status getResponseStatus() throws IOException {
        return status;
    }

    @Override
    public void setResponseStatus(Status status) throws IOException {
        this.status = status;
    }

    @Override
    public boolean isResponseSuccess() throws IOException {
        return status != null && status.isSuccess();
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }

        closed = true;

        IOKit.close(reader);
        IOKit.close(in);

        super.close();
    }

}
