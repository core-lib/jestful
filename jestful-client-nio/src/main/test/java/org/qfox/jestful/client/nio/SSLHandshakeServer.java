package org.qfox.jestful.client.nio;

import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.logging.Logger;

public class SSLHandshakeServer {

    private static Logger logger = Logger.getLogger(SSLHandshakeServer.class.getName());

    private SocketChannel sc;
    private SSLEngine sslEngine;
    private Selector selector;

    private ByteBuffer myNetData;
    private ByteBuffer myAppData;
    private ByteBuffer peerNetData;
    private ByteBuffer peerAppData;

    private ByteBuffer dummy = ByteBuffer.allocate(0);

    private HandshakeStatus hsStatus;
    private Status status;

    public void run() throws Exception {

        char[] password = "123456".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream in = this.getClass().getResourceAsStream("Users/yangchangpei/password.cer");
        keyStore.load(in, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, password);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), null, null);

        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);

        SSLSession session = sslEngine.getSession();
        myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        peerNetData.clear();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        selector = Selector.open();
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.bind(new InetSocketAddress(8443));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Server listens on port 8443... ...");

        while (true) {

            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                handleRequest(selectionKey);
            }
        }


    }

    private void handleRequest(SelectionKey key) throws Exception {

        if (key.isAcceptable()) {

            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel channel = ssc.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);

        } else if (key.isReadable()) {

            sc = (SocketChannel) key.channel();

            logger.info("Server handshake begins... ...");
            sslEngine.beginHandshake();
            hsStatus = sslEngine.getHandshakeStatus();
            doHandshake();
            if (hsStatus == HandshakeStatus.FINISHED) {
                key.cancel();
                sc.close();
            }
            logger.info("Server handshake completes... ...");

        }

    }

    private void doHandshake() throws IOException {

        SSLEngineResult result;

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

                    int count = sc.read(peerNetData);
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

                    assert status != status.BUFFER_OVERFLOW : "buffer not overflow." + status.toString();

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
                    sc.write(myNetData);

                    break;

            }
        }
    }

    public static void main(String[] args) throws Exception {
        new SSLHandshakeServer().run();
    }


}
