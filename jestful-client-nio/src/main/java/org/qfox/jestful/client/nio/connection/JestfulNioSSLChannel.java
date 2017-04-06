package org.qfox.jestful.client.nio.connection;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/4.
 * Version: 1.0
 */
public class JestfulNioSSLChannel implements NioSSLChannel {
    private SSLEngine sslEngine;
    private ByteBuffer netInputBuffer;
    private ByteBuffer appInputBuffer;
    private ByteBuffer netOutputBuffer;
    private ByteBuffer appOutputBuffer;

    public JestfulNioSSLChannel(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
        SSLSession session = sslEngine.getSession();
        appInputBuffer = ByteBuffer.allocate(session.getApplicationBufferSize());
        appInputBuffer.flip();
        netInputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());
        netInputBuffer.flip();
        appOutputBuffer = ByteBuffer.allocate(session.getApplicationBufferSize());
        appOutputBuffer.flip();
        netOutputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());
        netOutputBuffer.flip();
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        appOutputBuffer.compact();
        while (appOutputBuffer.hasRemaining() && buffer.hasRemaining()) appOutputBuffer.put(buffer.get());
        appOutputBuffer.flip();
    }

    @Override
    public void read(ByteBuffer buffer) throws IOException {
        while (appInputBuffer.hasRemaining() && buffer.hasRemaining()) buffer.put(appInputBuffer.get());
    }

    @Override
    public void copy(ByteBuffer buffer) throws IOException {
        int n = Math.min(netOutputBuffer.remaining(), buffer.remaining());
        buffer.put(netOutputBuffer.array(), netOutputBuffer.position(), n);
    }

    @Override
    public boolean move(int n) throws IOException {
        int m = Math.min(netOutputBuffer.remaining(), n);
        netOutputBuffer.position(netOutputBuffer.position() + m);
        n -= m;
        if (n > 0) {
            throw new IOException("illegal call");
        }
        handshake();
        return appOutputBuffer.remaining() == 0 && netOutputBuffer.remaining() == 0;
    }

    @Override
    public void load(ByteBuffer buffer) throws IOException {
        netInputBuffer.compact();
        while (netInputBuffer.hasRemaining() && buffer.hasRemaining()) netInputBuffer.put(buffer.get());
        netInputBuffer.flip();
        handshake();
    }

    private void handshake() throws IOException {
        SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
        switch (status) {
            case NOT_HANDSHAKING:
                // 数据出站
                netOutputBuffer.compact();
                sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                netOutputBuffer.flip();
                // 数据入站
                appInputBuffer.compact();
                sslEngine.unwrap(netInputBuffer, appInputBuffer);
                appInputBuffer.flip();
                break;
            case FINISHED:
                break;
            case NEED_TASK:
                for (Runnable task = sslEngine.getDelegatedTask(); task != null; task = null) task.run();
                break;
            case NEED_WRAP:
                netOutputBuffer.compact();
                sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                netOutputBuffer.flip();
                break;
            case NEED_UNWRAP:
                appInputBuffer.compact();
                sslEngine.unwrap(netInputBuffer, appInputBuffer);
                appInputBuffer.flip();
                break;
        }
    }

    @Override
    public void close() throws IOException {
        sslEngine.closeOutbound();
        sslEngine.closeInbound();
    }
}
