package org.qfox.jestful.weixin.media;

/**
 * 微信公众号永久素材项
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:40
 **/
public class Item {
    private String media_id;
    private String name;
    private long update_time;
    private String url;
    private Content content;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Item{" +
                "media_id='" + media_id + '\'' +
                ", name='" + name + '\'' +
                ", update_time=" + update_time +
                ", url='" + url + '\'' +
                ", content=" + content +
                '}';
    }
}
