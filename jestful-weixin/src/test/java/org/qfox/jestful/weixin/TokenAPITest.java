package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.weixin.token.TokenAPI;
import org.qfox.jestful.weixin.token.TokenGrantType;
import org.qfox.jestful.weixin.token.TokenRefreshResult;

/**
 * 微信通用网关API单元测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 13:16
 **/
public class TokenAPITest extends BaseAPITest {
    private final Object lock = new Object();
    private volatile String token;
    private volatile long timeExpired;

    private TokenAPI tokenAPI;

    @Override
    public void setup() throws Exception {
        super.setup();
        if (tokenAPI == null) tokenAPI = client.create(TokenAPI.class);
    }

    /**
     * 获取当前可用的token, 如果没有或已过时这个方法会自动去刷新并返回最新的
     *
     * @return 当前可用的token
     * @throws Exception 未知异常
     */
    protected String getAccessToken() throws Exception {
        if (System.currentTimeMillis() < timeExpired) return token;
        synchronized (lock) {
            if (System.currentTimeMillis() < timeExpired) return token;
            TokenRefreshResult result = tokenAPI.refresh(TokenGrantType.client_credential, APP_ID, APP_SECRET);
            assert result.success() : result.getErrmsg();
            token = result.getAccess_token();
            timeExpired = System.currentTimeMillis() + (result.getExpires_in() * 1000L) - (5L * 60L * 1000L); // 真正失效前五分钟认为失效
        }
        return token;
    }

    @Test
    public void refresh() throws Exception {
        String token = getAccessToken();
        System.out.println(token);
    }

}
