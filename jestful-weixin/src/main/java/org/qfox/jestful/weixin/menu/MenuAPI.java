package org.qfox.jestful.weixin.menu;

import org.qfox.jestful.core.http.*;

/**
 * 微信公众号菜单API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 15:11
 **/
@HTTP("/cgi-bin/menu")
public interface MenuAPI {

    @POST(value = "/create", consumes = "application/json", produces = "application/json")
    MenuCreateResult create(@Query("access_token") String accessToken, @Body Menu menu);

    @GET(value = "/get", produces = "application/json")
    MenuQueryResult query(@Query("access_token") String accessToken);

    @GET(value = "/delete", produces = "application/json")
    MenuDeleteResult delete(@Query("access_token") String accessToken);

}
