package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.commons.pool.ConcurrentPool;

import java.net.SocketAddress;

public class ConcurrentSocketChannelConnectionPool extends ConcurrentPool<SocketAddress, SocketChannelConnection> implements SocketChannelConnectionPool {
}
