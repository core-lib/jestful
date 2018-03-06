package org.qfox.jestful.weixin.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Button {
    private String name;
    private ButtonType type;
    private String key;
    private String url;
    private String mediaId;
    private String appId;
    private String path;
    private List<Button> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ButtonType getType() {
        return type;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("media_id")
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @JsonProperty("appid")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @JsonProperty("pagepath")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("sub_button")
    public List<Button> getChildren() {
        return children;
    }

    public void setChildren(List<Button> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Button{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", appId='" + appId + '\'' +
                ", path='" + path + '\'' +
                ", children=" + children +
                '}';
    }
}
