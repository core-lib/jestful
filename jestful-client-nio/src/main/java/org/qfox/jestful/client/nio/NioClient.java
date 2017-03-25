package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class NioClient extends Client implements Runnable, Registrations.Consumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static NioClient defaultClient;
    private Selector selector;
    private Registrations registrations;
    private final ByteBuffer buffer = ByteBuffer.allocate(8096);

    private NioClient(NioBuilder builder) {
        super(builder);
        Thread thread = new Thread(this);
        thread.start();
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }
    }

    @Override
    public void consume(SocketChannel channel, int options, Object attachment) {
        try {
            channel.register(selector, options, attachment);
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            registrations = new Registrations(selector);
            synchronized (this) {
                this.notifyAll();
            }
        } catch (Exception e) {
            throw new Error(e);
        }
        // 运行
        while (!isDestroyed()) {
            SelectionKey key = null;
            try {
                registrations.foreach(this);

                if (selector.select() == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        key.cancel();
                    } else if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            if (channel.finishConnect()) {
                                Action action = (Action) key.attachment();
                                channel.register(selector, SelectionKey.OP_WRITE, action);
                            }
                        }
                    } else if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Action action = (Action) key.attachment();
                        JestfulNioClientRequest request = (JestfulNioClientRequest) action.getExtra().get(JestfulNioClientRequest.class);
                        boolean finished = request.read(channel);
                        if (finished) {
                            channel.register(selector, SelectionKey.OP_READ, action);
                        }
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Action action = (Action) key.attachment();
                        JestfulNewClientResponse response = (JestfulNewClientResponse) action.getExtra().get(JestfulNewClientResponse.class);
                        buffer.clear();
                        channel.read(buffer);
                        buffer.flip();
                        boolean finished = response.receive(buffer);
                        if (finished) {
                            key.cancel();
                        }
                    } else {
                        key.cancel();
                    }
                }
            } catch (Throwable throwable) {
                if (key != null) {
                    key.cancel();
                }
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

            registrations.register(channel, SelectionKey.OP_CONNECT, action);

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
