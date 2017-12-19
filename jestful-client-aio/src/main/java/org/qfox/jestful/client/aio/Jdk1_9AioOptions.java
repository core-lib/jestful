package org.qfox.jestful.client.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class Jdk1_9AioOptions extends Jdk1_7AioOptions implements AioOptions {
    private Boolean so_reuseport;

    protected Jdk1_9AioOptions() {
    }

    public static Jdk1_9AioOptions create() {
        return new Jdk1_9AioOptions();
    }

    @Override
    public void config(AsynchronousSocketChannel channel) throws IOException {
//        if (so_reuseport != null) channel.setOption(StandardSocketOptions.SO_REUSEPORT, so_reuseport);
        super.config(channel);
    }

    public Boolean getSo_reuseport() {
        return so_reuseport;
    }

    public Jdk1_9AioOptions setSo_reuseport(Boolean so_reuseport) {
        this.so_reuseport = so_reuseport;
        return this;
    }
}
