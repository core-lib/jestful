package org.qfox.jestful.client.nio.connection;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/4/2.
 */
class HttpsNioSSLChannel implements NioSSLChannel {
    private final SSLEngine sslEngine;
    private final ByteBuffer netInputBuffer;
    private final ByteBuffer appInputBuffer;
    private final ByteBuffer netOutputBuffer;
    private final ByteBuffer appOutputBuffer;

    HttpsNioSSLChannel(SSLEngine sslEngine) {
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
    public boolean wrap(ByteBuffer from, ByteBuffer to) throws IOException {
        put(from, appOutputBuffer);
        doHandshake();
        while (to.hasRemaining() && netOutputBuffer.hasRemaining()) to.put(netOutputBuffer.get());
        return appOutputBuffer.remaining() == 0 && netOutputBuffer.remaining() == 0;
    }

    @Override
    public boolean unwrap(ByteBuffer from, ByteBuffer to) throws IOException {
        put(from, netInputBuffer);
        doHandshake();
        while (to.hasRemaining() && appInputBuffer.hasRemaining()) to.put(appInputBuffer.get());
        return netInputBuffer.remaining() == 0 && appInputBuffer.remaining() == 0;
    }

    private void put(ByteBuffer from, ByteBuffer to) {
        to.compact();
        while (from.hasRemaining() && to.hasRemaining()) to.put(from.get());
        to.flip();
    }

    private void doHandshake() throws IOException {
        SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
        switch (status) {
            case NOT_HANDSHAKING:
                // 数据出站
                netOutputBuffer.compact();
                sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                netOutputBuffer.flip();
                // 数据入站
                sslEngine.unwrap(netInputBuffer, appInputBuffer);
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
                sslEngine.unwrap(netInputBuffer, appInputBuffer);
                break;
        }
    }

}
