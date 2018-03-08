package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

import java.util.List;

/**
 * 微信公众号永久素材图文消息查询结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:03
 **/
public class MaterialQueryResult extends BaseResult {
    private List<Article> news_item;
    private String title;
    private String description;
    private String down_url;

    public List<Article> getNews_item() {
        return news_item;
    }

    public void setNews_item(List<Article> news_item) {
        this.news_item = news_item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    @Override
    public String toString() {
        return "MaterialQueryResult{" +
                "news_item=" + news_item +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", down_url='" + down_url + '\'' +
                "} " + super.toString();
    }
}
