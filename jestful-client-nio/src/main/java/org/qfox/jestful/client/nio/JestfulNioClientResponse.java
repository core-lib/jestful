package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Connector;
import org.qfox.jestful.client.JestfulClientResponse;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioClientResponse extends JestfulClientResponse {
    private final ByteArrayOutputStream head = new ByteArrayOutputStream();
    private final OutputStream data = new ByteArrayOutputStream();

    private Boolean chunked;
    private int total;
    private int position;

    private byte[] last;
    private int line;

    private Status status = new Status(200, "OK");

    protected JestfulNioClientResponse(Action action, Connector connector, Gateway gateway) {
        super(action, connector, gateway);
    }

    public boolean write(ByteBuffer buffer) throws IOException {
        if (buffer.remaining() == 0) {
            return false;
        }
        // 如果还有上次的 则放在前面
        byte[] bytes = new byte[buffer.remaining() + (last == null ? 0 : last.length)];
        if (last != null) {
            System.arraycopy(last, 0, bytes, 0, last.length);
        }
        buffer.get(bytes, last == null ? 0 : last.length, buffer.remaining());
        last = null;
        if (chunked == null) {
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                head.write(b);
                data.write(b);
                if (b == '\n') {
                    line++;
                    // header 读完
                    if (line == 2) {
                        ByteArrayInputStream in = new ByteArrayInputStream(head.toByteArray());
                        String head = IOUtils.readln(in);
                        status = new Status(head);

                        while (in.available() > 0) {
                            String line = IOUtils.readln(in);
                            int index = line.indexOf(':');
                            if (index == -1) {
                                continue;
                            }
                            String name = line.substring(0, index).trim();
                            String value = line.substring(index + 1).trim();
                            setResponseHeader(name, value);
                        }

                        String transferEncoding = getResponseHeader("Transfer-Encoding");
                        if (transferEncoding != null && transferEncoding.trim().toLowerCase().equals("chunked")) {
                            chunked = true;
                            total = 0;
                            position = 0;
                            last = new byte[]{'\r', '\n'};
                        } else {
                            chunked = false;
                            String contentLength = getResponseHeader("Content-Length");
                            total = contentLength != null && contentLength.matches("\\d+") ? Integer.valueOf(contentLength) : 0;
                            position = 0;
                        }

                        buffer = ByteBuffer.wrap(bytes, i + 1, bytes.length - i - 1);
                        return write(buffer);
                    }
                } else if (b != '\r') {
                    line = 0;
                }
            }
            return false;
        } else if (chunked) {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            int length;
            if (position >= total) {
                IOUtils.readln(in);

                // 如果没有包含换行符 则保存起来
                int count = 0;
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] == '\n') {
                        count++;
                        if (count == 2) {
                            break;
                        }
                    }
                }
                if (count < 2) {
                    last = bytes;
                    return false;
                }

                total = Integer.valueOf(IOUtils.readln(in), 16);
                position = 0;
                if (total == 0) {
                    IOUtils.writeln(Integer.toHexString(0), data);
                    IOUtils.writeln("", data);
                    return true;
                }
                length = Math.min(total, in.available());
                IOUtils.writeln(Integer.toHexString(total), data);
            } else {
                length = Math.min(total - position, in.available());
            }
            IOUtils.transfer(in, length, data);
            position += length;
            if (position == total) {
                IOUtils.writeln("", data);
            }
            length = in.available();
            in.read(bytes, 0, length);
            return write(ByteBuffer.wrap(bytes, 0, length));
        } else {
            data.write(bytes);
            position += bytes.length;
            return position >= total;
        }
    }

}
