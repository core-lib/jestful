package org.qfox.jestful.client.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface NioOptions {

    NioOptions DEFAULT = new NioOptions() {
        @Override
        public void config(SocketChannel channel) throws IOException {

        }
    };

    void config(SocketChannel channel) throws IOException;

}
