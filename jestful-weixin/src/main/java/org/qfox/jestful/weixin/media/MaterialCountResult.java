package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 微信公众号永久素材数量结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:27
 **/
public class MaterialCountResult extends BaseResult {
    private int voice_count;
    private int video_count;
    private int image_count;
    private int news_count;

    public int getVoice_count() {
        return voice_count;
    }

    public void setVoice_count(int voice_count) {
        this.voice_count = voice_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    @Override
    public String toString() {
        return "MaterialCountResult{" +
                "voice_count=" + voice_count +
                ", video_count=" + video_count +
                ", image_count=" + image_count +
                ", news_count=" + news_count +
                "} " + super.toString();
    }
}
