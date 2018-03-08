package org.qfox.jestful.weixin.customer;

import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.POST;
import org.qfox.jestful.core.http.Query;

/**
 * 微信公众号客服API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 10:23
 **/
@HTTP("/customservice/kfaccount")
public interface AccountAPI {

    @POST(value = "/add", produces = "application/json", consumes = "application/json")
    AccountCreateResult create(@Query("access_token") String token, @Body Account account);

}
