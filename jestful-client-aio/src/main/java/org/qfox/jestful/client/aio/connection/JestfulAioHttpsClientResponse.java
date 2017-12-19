package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulAioHttpsClientResponse extends JestfulAioHttpClientResponse {
    private final AioSSLChannel aioSSLChannel;
    private final ByteBuffer block = ByteBuffer.allocate(4096);

    protected JestfulAioHttpsClientResponse(Action action,
                                            Connector connector,
                                            Gateway gateway,
                                            AioSSLChannel aioSSLChannel) {
        super(action, connector, gateway);
        this.aioSSLChannel = aioSSLChannel;
    }

    @Override
    public void clear() {
        super.clear();
        if (this.block != null) this.block.clear();
    }

    @Override
    public boolean load(ByteBuffer buffer) throws IOException {
        // 这里非常重要!!!因为有可能客户端已经接收到了所有的服务端要发送过来的数据
        // 但是只从SSLEngine里面读取一次是不能完全读取出来的!!!
        // 需要重复读取到SSLEngine没有可读的数据或者回应体结束
        // 否则会造成程序会在 HTTPS 协议 而且开启了 Keep-Alive 模式读取比较大的回应体时莫名其妙的卡刚好一分钟最后收到31个byte(估计是心跳包)才顺利完成
        while (true) {
            aioSSLChannel.load(buffer);
            block.clear();
            aioSSLChannel.read(block);
            block.flip();
            // 没有可读得了
            if (block.remaining() == 0) return super.load(block);
            // 回应体读取结束
            if (super.load(block)) return true;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
