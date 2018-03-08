package org.qfox.jestful.weixin.media;


import org.qfox.jestful.weixin.BaseResult;

public class NewsUploadResult extends BaseResult {
    private String type;
    private String media_id;
    private long created_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "NewsUploadResult{" +
                "type='" + type + '\'' +
                ", media_id='" + media_id + '\'' +
                ", created_at=" + created_at +
                "} " + super.toString();
    }
}