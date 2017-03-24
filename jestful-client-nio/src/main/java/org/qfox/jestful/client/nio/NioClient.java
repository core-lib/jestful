package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class NioClient extends Client implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static NioClient defaultClient;

    private Selector selector;

    protected NioClient(NioBuilder builder) {
        super(builder);
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        // 启动
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("NioClient Start Fail", e);
            destroy();
            return;
        }
        // 运行
        while (!isDestroyed()) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        key.cancel();
                    } else if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else {
                                key.cancel();
                            }
                        }
                    } else if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        
                    } else if (key.isReadable()) {

                    } else {
                        key.cancel();
                    }
                }
            } catch (Throwable throwable) {
                logger.warn("", throwable);
            }
        }
    }

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        Response response = action.getResponse();
        try {
            Restful restful = action.getRestful();

            if (restful.isAcceptBody()) {
                serialize(action);
            } else {
                request.connect();
            }

            String protocol = action.getProtocol();
            String host = action.getHost();
            Integer port = action.getPort();
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port != null && port >= 0 ? port : "https".equalsIgnoreCase(protocol) ? 443 : "http".equalsIgnoreCase(protocol) ? 80 : 80));
            channel.register(selector, SelectionKey.OP_CONNECT, action);

            return null;
        } catch (Exception e) {
            IOUtils.close(request);
            IOUtils.close(response);
            throw e;
        }
    }

    @Override
    public <T> T create(Class<T> interfase, URL endpoint) {
        String protocol = endpoint.getProtocol();
        String host = endpoint.getHost();
        Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
        String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
        return new JestfulNioInvocationHandler<T>(interfase, protocol, host, port, route, this).getProxy();
    }

    public static NioClient getDefaultClient() {
        if (defaultClient != null) {
            return defaultClient;
        }
        synchronized (Client.class) {
            if (defaultClient != null) {
                return defaultClient;
            }
            return defaultClient = NioClient.builder().build();
        }
    }

    public static NioBuilder builder() {
        return new NioBuilder();
    }

    public static class NioBuilder extends Builder {

        @Override
        public NioClient build() {
            return new NioClient(this);
        }

    }

}
