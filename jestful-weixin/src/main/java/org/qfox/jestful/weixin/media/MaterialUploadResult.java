package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信公众号永久素材上传结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 13:45
 **/
public class MaterialUploadResult extends BaseResult {
    private String media_id;
    private String url;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MaterialUploadResult{" +
                "media_id='" + media_id + '\'' +
                ", url='" + url + '\'' +
                "} " + super.toString();
    }
}
