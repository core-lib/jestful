package org.qfox.jestful.weixin.media;

import java.util.List;

/**
 * 微信公众号图文素材
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 11:29
 **/
public class News {
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "News{" +
                "articles=" + articles +
                '}';
    }
}
