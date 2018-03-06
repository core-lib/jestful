package org.qfox.jestful.weixin.menu;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信公众号自定义菜单匹配结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 16:00
 **/
public class ConditionalMenuMatchResult extends BaseResult {
    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "ConditionalMenuMatchResult{" +
                "menu=" + menu +
                "} " + super.toString();
    }
}
