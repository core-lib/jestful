package org.qfox.jestful.client.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public interface AioOptions {

    AioOptions DEFAULT = new AioOptions() {
        @Override
        public void config(AsynchronousSocketChannel channel) throws IOException {

        }
    };

    void config(AsynchronousSocketChannel channel) throws IOException;

}
