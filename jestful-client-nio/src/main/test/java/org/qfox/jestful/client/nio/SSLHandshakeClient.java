package org.qfox.jestful.client.nio;

import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.logging.Logger;

public class SSLHandshakeClient {
    private static Logger logger = Logger.getLogger(SSLHandshakeClient.class.getName());


    private SocketChannel sc;


    private SSLEngine sslEngine;

    private Selector selector;


    private HandshakeStatus hsStatus;

    private Status status;


    private ByteBuffer myNetData;

    private ByteBuffer myAppData;

    private ByteBuffer peerNetData;

    private ByteBuffer peerAppData;


    private ByteBuffer dummy = ByteBuffer.wrap("GET /\r\n\r\n".getBytes());


    public void run() throws Exception {
//        char[] password = "123456".toCharArray();
//        KeyStore trustStore = KeyStore.getInstance("JKS");
//        InputStream in = this.getClass().getResourceAsStream("/Users/yangchangpei/truststore");
//        trustStore.load(in, password);
//
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//        tmf.init(trustStore);
//
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, null, null);

        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);

        SSLSession session = sslEngine.getSession();
        myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        peerNetData.clear();

        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("www.baidu.com", 443));

        sslEngine.beginHandshake();
        hsStatus = sslEngine.getHandshakeStatus();

        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                handleSocketEvent(selectionKey);
            }
        }
    }


    private void handleSocketEvent(SelectionKey key) throws IOException {

        if (key.isConnectable()) {
            sc = (SocketChannel) key.channel();
            if (sc.isConnectionPending()) {
                sc.finishConnect();
            }

            doHandshake();

            sc.register(selector, SelectionKey.OP_READ);
        } else if (key.isWritable()) {
            sc = (SocketChannel) key.channel();
            doHandshake();
        } else if (key.isReadable()) {
            sc = (SocketChannel) key.channel();
            doHandshake();
            if (hsStatus == HandshakeStatus.FINISHED) {
                logger.info("Client handshake completes... ...");
                key.cancel();
                sc.close();
            }
        }

    }


    private void doHandshake() throws IOException {

        SSLEngineResult result;
        int count = 0;

        while (hsStatus != HandshakeStatus.FINISHED) {

            logger.info("handshake status: " + hsStatus);
            switch (hsStatus) {
                case NEED_TASK:
                    Runnable runnable;
                    while ((runnable = sslEngine.getDelegatedTask()) != null) {
                        runnable.run();
                    }
                    hsStatus = sslEngine.getHandshakeStatus();
                    break;
                case NEED_UNWRAP:
                    count = sc.read(peerNetData);
                    if (count < 0) {
                        logger.info("no data is read for unwrap.");
                        break;
                    } else {
                        logger.info("data read: " + count);
                    }
                    peerNetData.flip();
                    peerAppData.clear();

                    do {
                        result = sslEngine.unwrap(peerNetData, peerAppData);
                        logger.info("Unwrapping:\n" + result);
                        // During an handshake renegotiation we might need to perform
                        // several unwraps to consume the handshake data.
                    } while (result.getStatus() == SSLEngineResult.Status.OK &&
                            result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP &&
                            result.bytesProduced() == 0);

                    if (peerAppData.position() == 0 &&
                            result.getStatus() == SSLEngineResult.Status.OK &&
                            peerNetData.hasRemaining()) {

                        result = sslEngine.unwrap(peerNetData, peerAppData);
                        logger.info("Unwrapping:\n" + result);

                    }

                    hsStatus = result.getHandshakeStatus();
                    status = result.getStatus();

                    assert status != Status.BUFFER_OVERFLOW : "buffer not overflow." + status.toString();

                    // Prepare the buffer to be written again.
                    peerNetData.compact();
                    // And the app buffer to be read.
                    peerAppData.flip();

                    break;

                case NEED_WRAP:

                    myNetData.clear();
                    result = sslEngine.wrap(dummy, myNetData);
                    hsStatus = result.getHandshakeStatus();
                    status = result.getStatus();

                    while (status != Status.OK) {
                        logger.info("status: " + status);
                        switch (status) {

                            case BUFFER_OVERFLOW:
                                break;

                            case BUFFER_UNDERFLOW:

                                break;

                        }

                    }
                    myNetData.flip();
                    count = sc.write(myNetData);
                    if (count <= 0) {
                        logger.info("No data is written.");
                    } else {
                        logger.info("Written data: " + count);
                    }

                    break;

            }
        }
    }


    public static void main(String[] args) throws Exception {
        new SSLHandshakeClient().run();
    }


}