package org.qfox.jestful.weixin.media;

/**
 * 微信公众号永久素材分页参数
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 14:34
 **/
public class Pagination {
    private MediaType type;
    private int offset;
    private int count;

    public Pagination() {
    }

    public Pagination(MediaType type, int offset, int count) {
        this.type = type;
        this.offset = offset;
        this.count = count;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "type=" + type +
                ", offset=" + offset +
                ", count=" + count +
                '}';
    }
}
