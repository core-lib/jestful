package org.qfox.jestful.client.gateway;

import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月13日 下午3:25:46
 * @since 1.0.0
 */
public interface Gateway {

    Gateway NULL = new Gateway() {

        public boolean isProxy() {
            return false;
        }

        public Proxy toProxy() {
            return null;
        }

        @Override
        public SocketAddress toSocketAddress() {
            return null;
        }

        public void onConnected(Action action) throws IOException {

        }

    };

    boolean isProxy();

    Proxy toProxy();

    SocketAddress toSocketAddress();

    void onConnected(Action action) throws IOException;

}
