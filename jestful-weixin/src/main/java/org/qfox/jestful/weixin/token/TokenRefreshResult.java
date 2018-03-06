package org.qfox.jestful.weixin.token;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信通用网关获取Token结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 11:25
 **/
public class TokenRefreshResult extends BaseResult {
    private String access_token;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "TokenRefreshResult{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                "} " + super.toString();
    }
}
