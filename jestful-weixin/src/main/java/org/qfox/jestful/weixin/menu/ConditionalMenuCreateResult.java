package org.qfox.jestful.weixin.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信公众号自定义菜单创建结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 15:47
 **/
public class ConditionalMenuCreateResult extends BaseResult {
    private String menuId;

    @JsonProperty("menuid")
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "ConditionalMenuCreateResult{" +
                "menuId='" + menuId + '\'' +
                "} " + super.toString();
    }
}
