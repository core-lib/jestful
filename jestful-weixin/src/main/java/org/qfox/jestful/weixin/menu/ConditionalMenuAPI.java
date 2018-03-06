package org.qfox.jestful.weixin.menu;

import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.POST;
import org.qfox.jestful.core.http.Query;

/**
 * 微信公众号自定义菜单API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 15:45
 **/
@HTTP("/cgi-bin/menu")
public interface ConditionalMenuAPI {

    @POST(value = "/addconditional", consumes = "application/json", produces = "application/json")
    ConditionalMenuCreateResult create(@Query("access_token") String token, @Body Menu menu);

    @POST(value = "/delconditional", consumes = "application/json", produces = "application/json")
    ConditionalMenuCreateResult delete(@Query("access_token") String token, @Body Menu menu);

    @POST(value = "/trymatch", consumes = "application/json", produces = "application/json")
    ConditionalMenuMatchResult match(@Query("access_token") String token, @Body User user);

}
