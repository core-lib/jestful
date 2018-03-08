package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信公众号素材上传结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 12:31
 **/
public class MediaUploadResult extends BaseResult {
    private MediaType type;
    private String media_id;
    private long create_at;

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "MediaUploadResult{" +
                "type=" + type +
                ", media_id='" + media_id + '\'' +
                ", create_at=" + create_at +
                "} " + super.toString();
    }
}
