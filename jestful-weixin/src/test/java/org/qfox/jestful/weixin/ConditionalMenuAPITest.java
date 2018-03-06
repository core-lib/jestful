package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.weixin.menu.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * 微信公众号自定义菜单API测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 15:50
 **/
public class ConditionalMenuAPITest extends TokenAPITest {
    private ConditionalMenuAPI conditionalMenuAPI;

    @Test
    public void create() throws Exception {
        Button follow = new Button();
        follow.setName("iOS + Man");
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

        MatchRule rule = new MatchRule();
        rule.setPlatform("1");
        rule.setSex("1");
        menu.setMatchRule(rule);

        ConditionalMenuCreateResult result = conditionalMenuAPI.create(getAccessToken(), menu);
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void delete() throws Exception {
        ConditionalMenuCreateResult result = conditionalMenuAPI.delete(getAccessToken(), new Menu("436927645"));
        assert result.success() : result.getErrmsg();
    }

    @Test
    public void match() throws Exception {
        ConditionalMenuMatchResult result = conditionalMenuAPI.match(getAccessToken(), new User("change1921"));
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

}
