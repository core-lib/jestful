package org.qfox.jestful.weixin.media;

/**
 * 微信公众号永久视频素材描述
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 13:43
 **/
public class Description {
    private String title;
    private String introduction;

    public Description() {
    }

    public Description(String title, String introduction) {
        this.title = title;
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Description{" +
                "title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
