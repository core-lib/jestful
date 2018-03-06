package org.qfox.jestful.weixin.token;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Query;

/**
 * 微信通用网关接口API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 11:24
 **/
@HTTP("/cgi-bin")
public interface TokenAPI {

    /**
     * 获取微信通用网关API的Token令牌
     *
     * @param grantType 授权类型
     * @param appId     APP ID
     * @param appSecret APP Secret
     * @return Token令牌获取结果
     * @url https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APP_ID&secret=APP_SECRET
     */
    @GET(value = "/token", produces = "application/json")
    TokenRefreshResult refresh(
            @Query("grant_type") TokenGrantType grantType,
            @Query("appid") String appId,
            @Query("secret") String appSecret
    );

}
