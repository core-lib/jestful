package org.qfox.jestful.weixin.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ButtonType {
    click,
    view,
    scancode_push,
    scancode_waitmsg,
    pic_sysphoto,
    pic_photo_or_album,
    pic_weixin,
    location_select,
    media_id,
    view_limited,
    miniprogram;

    @JsonCreator
    public static ButtonType constantOf(String name) {
        for (ButtonType constant : values()) {
            if (constant.name().equals(name)) {
                return constant;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.name();
    }
}
