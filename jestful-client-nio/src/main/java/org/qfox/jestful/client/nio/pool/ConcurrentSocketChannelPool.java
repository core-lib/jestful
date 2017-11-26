package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.commons.pool.ConcurrentPool;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class ConcurrentSocketChannelPool extends ConcurrentPool<SocketAddress, SocketChannel> implements SocketChannelPool {
}
