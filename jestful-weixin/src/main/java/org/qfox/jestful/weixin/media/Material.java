package org.qfox.jestful.weixin.media;

/**
 * 微信公众号媒体
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:04
 **/
public class Material {
    private String media_id;

    public Material() {
    }

    public Material(String media_id) {
        this.media_id = media_id;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Override
    public String toString() {
        return "Material{" +
                "media_id='" + media_id + '\'' +
                '}';
    }
}
