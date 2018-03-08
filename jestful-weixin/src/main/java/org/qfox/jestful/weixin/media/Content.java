package org.qfox.jestful.weixin.media;

import java.util.List;

/**
 * 微信公众号永久图文素材内容
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:41
 **/
public class Content {
    private List<Article> news_item;

    public List<Article> getNews_item() {
        return news_item;
    }

    public void setNews_item(List<Article> news_item) {
        this.news_item = news_item;
    }

    @Override
    public String toString() {
        return "Content{" +
                "news_item=" + news_item +
                '}';
    }
}
