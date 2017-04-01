package org.qfox.jestful.tutorial;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.Iterator;

public class NioSSLServer {
    private SSLEngine sslEngine;
    private Selector selector;
    private SSLContext sslContext;
    private ByteBuffer netInputBuffer;
    private ByteBuffer appInputBuffer;
    private ByteBuffer netOutputBuffer;
    private ByteBuffer appOutputBuffer;
    private static final String SSL_TYPE = "SSL";
    private static final String KS_TYPE = "JKS";
    private static final String X509 = "SunX509";
    private final static int PORT = 8443;

    public void run() throws Exception {
        createServerSocket();
        createSSLContext();
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

    private void createBuffer() {
        SSLSession session = sslEngine.getSession();
        appInputBuffer = ByteBuffer.allocate(session.getApplicationBufferSize());
        netInputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());

        appOutputBuffer = ByteBuffer.wrap(("HTTP/1.1 200 OK\r\nContent-Length:2\r\n\r\nOK").getBytes());
        netOutputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());
    }

    private void createSSLEngine() {
        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
    }

    private void createServerSocket() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        selector = Selector.open();
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.bind(new InetSocketAddress(PORT));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void createSSLContext() throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(X509);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
        String serverKeyStoreFile = "C:\\Users\\payne\\csii.jks";
        String svrPassphrase = "123456";
        char[] svrPassword = svrPassphrase.toCharArray();
        KeyStore serverKeyStore = KeyStore.getInstance(KS_TYPE);
        serverKeyStore.load(new FileInputStream(serverKeyStoreFile), svrPassword);
        kmf.init(serverKeyStore, svrPassword);
        String clientKeyStoreFile = "C:\\Users\\payne\\csii_pub.jks";
        String cntPassphrase = "123456";
        char[] cntPassword = cntPassphrase.toCharArray();
        KeyStore clientKeyStore = KeyStore.getInstance(KS_TYPE);
        clientKeyStore.load(new FileInputStream(clientKeyStoreFile), cntPassword);
        tmf.init(clientKeyStore);
        sslContext = SSLContext.getInstance(SSL_TYPE);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    }

    private void handleRequest(SelectionKey key) throws Exception {
        if (key.isAcceptable()) {
            createSSLEngine();
            createBuffer();
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel channel = ssc.accept();
            channel.configureBlocking(false);
            doHandshake(channel);
        } else if (key.isReadable()) {
            if (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                SocketChannel sc = (SocketChannel) key.channel();
                netInputBuffer.clear();
                appInputBuffer.clear();
                sc.read(netInputBuffer);
                netInputBuffer.flip();
                SSLEngineResult engineResult = sslEngine.unwrap(netInputBuffer, appInputBuffer);
                doTask();
                if (engineResult.getStatus() == SSLEngineResult.Status.OK) {
                    appInputBuffer.flip();

                }
                sc.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (key.isWritable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            netOutputBuffer.clear();
            sslEngine.wrap(appOutputBuffer, netOutputBuffer);
            doTask();
            netOutputBuffer.flip();
            if (netOutputBuffer.hasRemaining()) sc.write(netOutputBuffer);
            else key.cancel();
        }
    }

    private boolean handshakeStarted = false;

    private void doHandshake(SocketChannel sc) throws IOException {
        boolean done = false;
        if (!handshakeStarted) {
            sslEngine.beginHandshake();
        }
        SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
        while (!done) {
            switch (status) {
                case FINISHED:
                    break;
                case NEED_TASK:
                    status = doTask();
                    break;
                case NEED_UNWRAP:
                    netInputBuffer.clear();
                    sc.read(netInputBuffer);
                    netInputBuffer.flip();
                    do {
                        sslEngine.unwrap(netInputBuffer, appInputBuffer);
                        status = doTask();
                    } while (status == SSLEngineResult.HandshakeStatus.NEED_UNWRAP && netInputBuffer.remaining() > 0);
                    netInputBuffer.clear();
                    break;
                case NEED_WRAP:
                    sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                    status = doTask();
                    netOutputBuffer.flip();
                    sc.write(netOutputBuffer);
                    netOutputBuffer.clear();
                    break;
                case NOT_HANDSHAKING:
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    done = true;
                    break;
            }
        }
    }

    private SSLEngineResult.HandshakeStatus doTask() {
        for (Runnable task = sslEngine.getDelegatedTask(); task != null; task = sslEngine.getDelegatedTask()) {
            task.run();
        }
        return sslEngine.getHandshakeStatus();
    }

    public static void main(String[] args) throws Exception {
        new NioSSLServer().run();
    }
}