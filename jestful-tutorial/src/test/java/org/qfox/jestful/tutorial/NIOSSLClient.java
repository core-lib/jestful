package org.qfox.jestful.tutorial;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by payne on 2017/4/1.
 */
public class NIOSSLClient {
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);

    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("Hello World!".getBytes());
        buffer.flip();
        buffer.get(new byte[6]);
        buffer.compact();
        buffer.put("NIOSSL".getBytes());
        buffer.flip();
        System.out.println(new String(buffer.array(), buffer.arrayOffset(), buffer.remaining()));

        new NIOSSLClient().run();
    }

    public void run() throws Exception {
        createSSLContext();
        createSSLEngine();
        createBuffer();

        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("merchant.qfoxy.com", 443));
        channel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    // 1. 建立连接
                    if (channel.isConnectionPending() && channel.finishConnect()) {
                        // 2. 建立成功后注册读写事件
                        channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        // 3. 申请SSL握手
                        sslEngine.beginHandshake();
                    }
                }
                // 4. 一般来说一直都可以写
                if (key.isWritable()) {
                    // 5. 写到流里面
                    channel.write(netOutputBuffer);
                }
                // 6. 如果可以读 有可能是服务端发来的握手数据
                if (key.isReadable()) {
                    // 7. 有可能之前的数据还没消费完 所以接收到的数据应该放在netInputBuffer的后面
                    netInputBuffer.compact();
                    channel.read(netInputBuffer);
                    netInputBuffer.flip();
                }
                // 8. 尝试再握手
                doHandshake();
            }
        }
    }

    private void doHandshake() throws IOException {
        SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
        switch (status) {
            case NOT_HANDSHAKING:
                System.out.println("握手完成");
                // 数据出站
                netOutputBuffer.compact();
                sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                netOutputBuffer.flip();
                // 数据入站
                sslEngine.unwrap(netInputBuffer, appInputBuffer);
                break;
            case FINISHED:
                System.out.println("握手结束");
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

    public void start() throws Exception {
        createSSLContext();
        createSSLEngine();
        createBuffer();

        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("merchant.qfoxy.com", 443));
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
                        while (true) {
                            SSLEngineResult.HandshakeStatus status = sslEngine.getHandshakeStatus();
                            switch (status) {
                                case NOT_HANDSHAKING:
                                    if (appOutputBuffer.hasRemaining()) {
                                        netOutputBuffer.clear();
                                        SSLEngineResult re = sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                                        System.out.println("wrap: " + re);
                                        netOutputBuffer.flip();
                                        System.out.println("need write : " + netOutputBuffer.remaining());
                                        System.out.println("written : " + channel.write(netOutputBuffer));
                                    } else {
                                        if (netInputBuffer.hasRemaining()) {
                                            SSLEngineResult r = sslEngine.unwrap(netInputBuffer, appInputBuffer);
                                            System.out.println("unwrap: " + r);
                                        } else {
                                            netInputBuffer.clear();
                                            System.out.println("read : " + channel.read(netInputBuffer));
                                            netInputBuffer.flip();
                                        }
                                    }
                                    break;
                                case FINISHED:
                                    break;
                                case NEED_TASK:
                                    for (Runnable task = sslEngine.getDelegatedTask(); task != null; task = sslEngine.getDelegatedTask()) {
                                        task.run();
                                    }
                                    break;
                                case NEED_WRAP:
                                    netOutputBuffer.clear();
                                    SSLEngineResult result = sslEngine.wrap(appOutputBuffer, netOutputBuffer);
                                    System.out.println("wrap: " + result);
                                    netOutputBuffer.flip();
                                    System.out.println("need write : " + netOutputBuffer.remaining());
                                    System.out.println("written : " + channel.write(netOutputBuffer));
                                    break;
                                case NEED_UNWRAP:
                                    if (netInputBuffer.hasRemaining()) {
                                        SSLEngineResult r = sslEngine.unwrap(netInputBuffer, appInputBuffer);
                                        System.out.println("unwrap: " + r);
                                    } else {
                                        netInputBuffer.clear();
                                        System.out.println("read : " + channel.read(netInputBuffer));
                                        netInputBuffer.flip();
                                    }
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
        netInputBuffer.flip();

        String request = "" +
                "GET / HTTP/1.1\n" +
                "Host: merchant.qfoxy.com\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate, sdch, br\n" +
                "Accept-Language: zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,da;q=0.2\n" +
                "Cookie: JSESSIONID=3FEB65C3A8B16B937C8B47CB28F4D9BE\n\n";
        appOutputBuffer = ByteBuffer.wrap(request.getBytes());
        netOutputBuffer = ByteBuffer.allocate(session.getPacketBufferSize());
        netOutputBuffer.flip();
    }

    private void createSSLEngine() {
        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
    }

    private void createSSLContext() throws Exception {
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
//        String clientKeyStoreFile = "C:\\Users\\payne\\csii_pub.jks";
//        String cntPassphrase = "123456";
//        char[] cntPassword = cntPassphrase.toCharArray();
//        KeyStore clientKeyStore = KeyStore.getInstance(KS_TYPE);
//        clientKeyStore.load(new FileInputStream(clientKeyStoreFile), cntPassword);
//        tmf.init(clientKeyStore);
        sslContext = SSLContext.getInstance(SSL_TYPE);
        sslContext.init(null, null, null);
    }

}
