package org.qfox.jestful.tutorial;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.Iterator;

/**
 * Created by payne on 2017/4/1.
 */
public class NIOSSLClient {
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);

    public static void main(String[] args) throws Exception {
        new NIOSSLClient().start();
    }

    public void start() throws Exception {
        createSSLContext();
        createSSLEngine();
        createBuffer();

        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 8443));
        channel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    if (channel.isConnectionPending() && channel.finishConnect()) {
                        sslEngine.beginHandshake();
                        while(true) {
                            SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
                            switch (status) {
                                case NOT_HANDSHAKING:
                                    break;
                                case FINISHED:
                                    break;
                                case NEED_TASK:
                                    for (Runnable task = sslEngine.getDelegatedTask(); task != null; task = sslEngine.getDelegatedTask()) {
                                        task.run();
                                    }
                                    break;
                                case NEED_WRAP:
                                    SSLEngineResult result = sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                                    netOutputBuffer.flip();
                                    channel.write(netOutputBuffer);
                                    break;
                                case NEED_UNWRAP:
                                    netInputBuffer.clear();
                                    channel.read(netInputBuffer);
                                    netInputBuffer.flip();
                                    SSLEngineResult r = sslEngine.unwrap(netInputBuffer, appInputBuffer);
                                    break;
                            }
                        }
                    }
                } else if (key.isWritable()) {

                } else if (key.isReadable()) {

                }
            }
        }
    }

    private SSLEngine sslEngine;
    private SSLContext sslContext;
    private ByteBuffer netInputBuffer;
    private ByteBuffer appInputBuffer;
    private ByteBuffer netOutputBuffer;
    private ByteBuffer appOutputBuffer;
    private static final String SSL_TYPE = "SSL";
    private static final String KS_TYPE = "JKS";
    private static final String X509 = "SunX509";

    private void createBuffer() {
        SSLSession session = sslEngine.getSession();
        appInputBuffer = ByteBuffer.allocate(session.getApplicationBufferSize());
        netInputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());

        appOutputBuffer = ByteBuffer.wrap(("HTTP/1.1 200 OK\r\nContent-Length:2\r\n\r\nOK").getBytes());
        netOutputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());
    }

    private void createSSLEngine() {
        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
    }

    private void createSSLContext() throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
        String clientKeyStoreFile = "C:\\Users\\payne\\csii_pub.jks";
        String cntPassphrase = "123456";
        char[] cntPassword = cntPassphrase.toCharArray();
        KeyStore clientKeyStore = KeyStore.getInstance(KS_TYPE);
        clientKeyStore.load(new FileInputStream(clientKeyStoreFile), cntPassword);
        tmf.init(clientKeyStore);
        sslContext = SSLContext.getInstance(SSL_TYPE);
        sslContext.init(null, tmf.getTrustManagers(), null);
    }

}
