package org.qfox.jestful.weixin.media;

import org.qfox.jestful.weixin.BaseResult;

import java.util.List;

/**
 * 微信公众号永久素材分页获取结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:38
 **/
public class MaterialListResult extends BaseResult {
    private int total_count;
    private int item_count;
    private List<Item> item;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "MaterialListResult{" +
                "total_count=" + total_count +
                ", item_count=" + item_count +
                ", item=" + item +
                "} " + super.toString();
    }
}
