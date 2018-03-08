package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

/**
 * 图片上传结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 11:03
 **/
public class ImageUploadResult extends BaseResult {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageUploadResult{" +
                "url='" + url + '\'' +
                "} " + super.toString();
    }
}
