package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.weixin.media.*;

import java.io.File;
import java.util.Collections;

/**
 * 微信公众号永久素材管理API测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 13:36
 **/
public class MaterialAPITest extends TokenAPITest {
    private MaterialAPI materialAPI;
    private File image = new File("D:\\用户目录\\我的图片\\新建文件夹\\16pic_5255774_b.jpg");
    private File video = new File("C:\\Users\\Administrator\\Desktop\\0b67231f70ae83a630ce5a2d76aaef43.mp4");

    @Test
    public void upload() throws Exception {
        // 上传图片
        {
            MaterialUploadResult result = materialAPI.upload(getAccessToken(), MediaType.image, image, null);
            assert result.success() : result.getErrmsg();
            System.out.println(result);
        }
        // 上传视频
        {
            MaterialUploadResult result = materialAPI.upload(getAccessToken(), MediaType.video, video, new Description("卡秀", "卡秀"));
            assert result.success() : result.getErrmsg();
            System.out.println(result);
        }
    }

    @Test
    public void query() throws Exception {
        MaterialQueryResult result = materialAPI.query(getAccessToken(), new Material("tjmcj9rc7UQ3aHf0mqVcVkSqpPKA8_xLV4TLc2JTwsU"));
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void download() throws Exception {
        File image = materialAPI.download(getAccessToken(), new Material("tjmcj9rc7UQ3aHf0mqVcVk34epwOsD_QcupJyWBGNUU"));
        System.out.println(image);
    }

    @Test
    public void delete() throws Exception {
        MaterialDeleteResult result = materialAPI.delete(getAccessToken(), new Material("tjmcj9rc7UQ3aHf0mqVcVk34epwOsD_QcupJyWBGNUU"));
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void uploadNews() throws Exception {
        News news = new News();
        Article article = new Article();
        article.setAuthor("Payne");
        article.setTitle("Title");
        article.setContent("Content");
        article.setThumb_media_id("tjmcj9rc7UQ3aHf0mqVcVvE8IEZyvccFC64QzS1UNvs");
        news.setArticles(Collections.singletonList(article));
        NewsUploadResult result = materialAPI.upload(getAccessToken(), news);
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void count() throws Exception {
        MaterialCountResult result = materialAPI.count(getAccessToken());
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void list() throws Exception {
        {
            MaterialListResult images = materialAPI.list(getAccessToken(), new Pagination(MediaType.image, 0, 20));
            assert images.success() : images.getErrmsg();
            System.out.println(images);
        }
        {
            MaterialListResult videos = materialAPI.list(getAccessToken(), new Pagination(MediaType.video, 0, 20));
            assert videos.success() : videos.getErrmsg();
            System.out.println(videos);
        }
        {
            MaterialListResult news = materialAPI.list(getAccessToken(), new Pagination(MediaType.news, 0, 20));
            assert news.success() : news.getErrmsg();
            System.out.println(news);
        }
        {
            MaterialListResult voices = materialAPI.list(getAccessToken(), new Pagination(MediaType.voice, 0, 20));
            assert voices.success() : voices.getErrmsg();
            System.out.println(voices);
        }
    }

}
