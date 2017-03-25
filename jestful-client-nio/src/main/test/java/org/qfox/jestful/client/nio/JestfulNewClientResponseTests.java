package org.qfox.jestful.client.nio;

import org.junit.Test;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/25.
 */
public class JestfulNewClientResponseTests {
    private NioByteArrayOutputStream head = new NioByteArrayOutputStream();
    private NioByteArrayOutputStream body = new NioByteArrayOutputStream();

    private Boolean chunked;
    private int ctrls;

    private Status status;

    private int total;
    private int position;

    private ByteBuffer last;

    protected final Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();

    @Test
    public void testReceiveChunked() throws Exception {
        JestfulNewClientResponseTests response = new JestfulNewClientResponseTests();
        FileInputStream fis = new FileInputStream("/Users/yangchangpei/Project/IntelliJ/jestful/jestful-client-nio/src/main/test/java/org/qfox/jestful/client/nio/chunked.txt");
        NioByteArrayOutputStream nbaos = new NioByteArrayOutputStream();
        IOUtils.transfer(fis, nbaos);
        ByteBuffer buffer = nbaos.toByteBuffer();
        boolean finished = response.receive(buffer);
        System.out.println(finished);
    }

    public String getResponseHeader(String name) {
        return header.containsKey(name) ? header.get(name)[0] : null;
    }

    public void setResponseHeader(String name, String value) {
        header.put(name, new String[]{value});
    }

    private void doReadHeader() throws IOException {
        InputStream in = head.toInputStream();
        String head = IOUtils.readln(in);
        status = new Status(head);

        while (in.available() > 0) {
            String line = IOUtils.readln(in);
            int index = line.indexOf(':');
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

    public boolean receive(ByteBuffer buffer) throws IOException {
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
                        if (++ctrls == 2) {
                            doReadHeader();
                            ctrls = 0;
                            return receive(buffer);
                        }
                        break;
                    case '\r':
                        break;
                    default:
                        ctrls = 0;
                        break;
                }
            }
        }
        // Transfer-Encoding == chunked
        else if (chunked) {
            // 上一段已读完 或者刚开始读回应体
            if (position == total) {
                // 读一行 注意这里有可能被NIO截断 需要等待下一次或多次 读取才能保证读到一行
                NioByteArrayOutputStream cache = new NioByteArrayOutputStream();
                // 把上次留下来的加到最开始位置
                while (last.hasRemaining()) {
                    byte b = last.get();
                    cache.write(b);
                    if (b == '\n') {
                        ctrls++;
                    }
                }
                // 再往后面追加
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    cache.write(b);
                    // 利用短路原理 b != '\n' 时 不会执行 ++ctrls !!!
                    if (b == '\n' && ++ctrls == 2) {
                        // 开始读取chunk size
                        InputStream in = cache.toInputStream();
                        // 去掉一个空行
                        IOUtils.readln(in);
                        // 紧跟着的一行就是chunk size
                        total = Integer.valueOf(IOUtils.readln(in), 16);
                        position = 0;
                        ctrls = 0;
                        if (total == 0) {
                            return true;
                        }
                        // 递归读取段内容
                        return receive(buffer);
                    }
                }
                // 来到这里证明真的在 chunk size 的位置被截断了 那么保留下来 等待下次接收的时候再确定 chunk size
                ctrls = 0;
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
                        return receive(buffer);
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

}