package org.qfox.jestful.weixin.menu;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.qfox.jestful.weixin.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by payne on 2017/2/19.
 */
public class MenuQueryResult extends BaseResult {
    private Menu menu;
    private List<Menu> conditionalMenus = new ArrayList<>();

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @JsonProperty("conditionalmenu")
    public List<Menu> getConditionalMenus() {
        return conditionalMenus;
    }

    public void setConditionalMenus(List<Menu> conditionalMenus) {
        this.conditionalMenus = conditionalMenus;
    }

    @Override
    public String toString() {
        return "MenuQueryResult{" +
                "menu=" + menu +
                ", conditionalMenus=" + conditionalMenus +
                "} " + super.toString();
    }
}
