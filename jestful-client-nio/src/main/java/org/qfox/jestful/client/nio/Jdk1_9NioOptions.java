package org.qfox.jestful.client.nio;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;

public class Jdk1_9NioOptions extends Jdk1_7NioOptions implements NioOptions {
    private Boolean so_reuseport;

    public static Jdk1_9NioOptions create() {
        return new Jdk1_9NioOptions();
    }

    @Override
    public void config(SocketChannel channel) throws IOException {
        if (so_reuseport != null) channel.setOption(StandardSocketOptions.SO_REUSEPORT, so_reuseport);
        super.config(channel);
    }

    public Jdk1_9NioOptions setSo_reuseport(Boolean so_reuseport) {
        this.so_reuseport = so_reuseport;
        return this;
    }

    public Boolean getSo_reuseport() {
        return so_reuseport;
    }
}
