package org.qfox.jestful.weixin.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
    private static final long serialVersionUID = -7846536313245561677L;

    private String menuId;
    private List<Button> buttons = new ArrayList<>();
    private MatchRule matchRule;

    public Menu() {
    }

    public Menu(String menuId) {
        this.menuId = menuId;
    }

    public Menu(List<Button> buttons) {
        this.buttons = buttons;
    }

    @JsonProperty("menuid")
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @JsonProperty("button")
    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    @JsonProperty("matchrule")
    public MatchRule getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(MatchRule matchRule) {
        this.matchRule = matchRule;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", buttons=" + buttons +
                ", matchRule=" + matchRule +
                '}';
    }
}
