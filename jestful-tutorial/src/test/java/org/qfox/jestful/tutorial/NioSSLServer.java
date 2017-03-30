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
    private ByteBuffer netInData;
    private ByteBuffer appInData;
    private ByteBuffer netOutData;
    private ByteBuffer appOutData;
    private static final String SSL_TYPE = "SSL";
    private static final String KS_TYPE = "JKS";
    private static final String X509 = "SunX509";
    private final static int PORT = 443;

    public void run() throws Exception {
        createServerSocket();
        createSSLContext();
        createSSLEngine();
        createBuffer();
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
        appInData = ByteBuffer.allocate(session.getApplicationBufferSize());
        netInData = ByteBuffer.allocate(session.getPacketBufferSize());
        appOutData = ByteBuffer.wrap("Hello\n".getBytes());
        netOutData = ByteBuffer.allocate(session.getPacketBufferSize());
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
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel channel = ssc.accept();
            channel.configureBlocking(false);
            doHandShake(channel);
        } else if (key.isReadable()) {
            if (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                SocketChannel sc = (SocketChannel) key.channel();
                netInData.clear();
                appInData.clear();
                sc.read(netInData);
                netInData.flip();
                SSLEngineResult engineResult = sslEngine.unwrap(netInData, appInData);
                doTask();
                if (engineResult.getStatus() == SSLEngineResult.Status.OK) {
                    appInData.flip();
                    System.out.println(new String(appInData.array()));

                }
                sc.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (key.isWritable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            netOutData.clear();
            SSLEngineResult engineResult = sslEngine.wrap(appOutData, netOutData);
            doTask();
            netOutData.flip();
            while (netOutData.hasRemaining())
                sc.write(netOutData);
            sc.register(selector, SelectionKey.OP_READ);
        }
    }

    private void doHandShake(SocketChannel sc) throws IOException {
        boolean handshakeDone = false;
        sslEngine.beginHandshake();
        SSLEngineResult.HandshakeStatus hsStatus = sslEngine.getHandshakeStatus();
        while (!handshakeDone) {
            switch (hsStatus) {
                case FINISHED:
                    break;
                case NEED_TASK:
                    hsStatus = doTask();
                    break;
                case NEED_UNWRAP:
                    netInData.clear();
                    sc.read(netInData);
                    netInData.flip();
                    do {
                        SSLEngineResult engineResult = sslEngine.unwrap(netInData, appInData);
                        hsStatus = doTask();
                    } while (hsStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP
                            && netInData.remaining() > 0);
                    netInData.clear();
                    break;
                case NEED_WRAP:
                    SSLEngineResult engineResult = sslEngine.wrap(appOutData, netOutData);
                    hsStatus = doTask();
                    netOutData.flip();
                    sc.write(netOutData);
                    netOutData.clear();
                    break;
                case NOT_HANDSHAKING:
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    handshakeDone = true;
                    break;
            }
        }
    }

    private SSLEngineResult.HandshakeStatus doTask() {
        Runnable task;
        while ((task = sslEngine.getDelegatedTask()) != null) {
            new Thread(task).start();
        }
        return sslEngine.getHandshakeStatus();
    }

    public static void main(String[] args) throws Exception {
        new NioSSLServer().run();
    }
}