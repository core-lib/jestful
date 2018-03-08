package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.weixin.media.*;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * 微信公众号临时素材管理API测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:12
 **/
public class MediaAPITest extends TokenAPITest {
    private MediaAPI mediaAPI;
    private File image = new File("D:\\用户目录\\我的图片\\新建文件夹\\16pic_5255774_b.jpg");

    @Test
    public void uploadSync() throws Exception {
        ImageUploadResult result = mediaAPI.upload(getAccessToken(), image);
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void uploadAsync() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        mediaAPI.upload(getAccessToken(), image, new Callback<ImageUploadResult>() {
            @Override
            public void onCompleted(boolean success, ImageUploadResult result, Exception exception) {
                latch.countDown();
            }

            @Override
            public void onSuccess(ImageUploadResult result) {
                assert result.success() : result.getErrmsg();
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }
        });
        latch.await();
    }

    @Test
    public void uploadAsyncLambda() throws Exception {
        CountDownLatch latch = new CountDownLatch(4);

        mediaAPI.upload(getAccessToken(), image,
                (success, result, exception) -> {
                    latch.countDown();
                    if (success) {
                        assert result.success() : result.getErrmsg();
                        System.out.println(result);
                    } else {
                        exception.printStackTrace();
                    }
                });

        mediaAPI.upload(getAccessToken(), image,
                result -> {
                    latch.countDown();
                    assert result.success() : result.getErrmsg();
                    System.out.println(result);
                });

        mediaAPI.upload(getAccessToken(), image,
                result -> {
                    latch.countDown();
                    assert result.success() : result.getErrmsg();
                    System.out.println(result);
                }, exception -> {
                    latch.countDown();
                    exception.printStackTrace();
                });

        mediaAPI.upload(getAccessToken(), image,
                result -> {
                    assert result.success() : result.getErrmsg();
                    System.out.println(result);
                },
                Throwable::printStackTrace,
                (success, result, exception) -> latch.countDown());

        latch.await();
    }

    @Test
    public void uploadMedia() throws Exception {
        MediaUploadResult result = mediaAPI.upload(getAccessToken(), MediaType.image, image);
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
        article.setThumb_media_id("TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2");
        news.setArticles(Collections.singletonList(article));
        NewsUploadResult result = mediaAPI.upload(getAccessToken(), news);
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

    @Test
    public void download() throws Exception {
        File file = mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2");
        System.out.println(file);

        CountDownLatch latch = new CountDownLatch(5);
        mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2", new Callback<File>() {
            @Override
            public void onCompleted(boolean success, File result, Exception exception) {
                latch.countDown();
            }

            @Override
            public void onSuccess(File result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }
        });

        mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2",
                (success, result, exception) -> {
                    latch.countDown();
                    if (success) System.out.println(result);
                    else exception.printStackTrace();
                });

        mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2",
                result -> {
                    latch.countDown();
                    System.out.println(result);
                });

        mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2",
                result -> {
                    latch.countDown();
                    System.out.println(result);
                }, exception -> {
                    latch.countDown();
                    exception.printStackTrace();
                });

        mediaAPI.download(getAccessToken(), "TOhaNmvlIylFuEa9U9OXCX4Z132V2d1GoqspbmN7cg1SH6Cm0WD4MlEgI51VY1Q2",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, exception) -> latch.countDown());

        latch.await();

    }

}
