package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.weixin.menu.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * 微信公众号菜单API测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 15:17
 **/
public class MenuAPITest extends TokenAPITest {
    private MenuAPI menuAPI;

    @Test
    public void create() throws Exception {
        Button follow = new Button();
        follow.setName("跟单");
        follow.setType(ButtonType.click);
        follow.setKey("follow");

        Button contact = new Button();
        contact.setName("联系我们");
        contact.setType(ButtonType.view);
        contact.setUrl("https://www.baidu.com");

        Button about = new Button();
        about.setName("关于");
        about.setChildren(Collections.singletonList(contact));

        Menu menu = new Menu(Arrays.asList(follow, about));
        MenuCreateResult result = menuAPI.create(getAccessToken(), menu);
        assert result.success() : result.getErrmsg();
    }

    @Test
    public void query() throws Exception {
        MenuQueryResult result = menuAPI.query(getAccessToken());
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void delete() throws Exception {
        MenuDeleteResult result = menuAPI.delete(getAccessToken());
        assert result.success() : result.getErrmsg();
    }

}
