package org.qfox.elasticsearch.node;

/**
 * 节点版本
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:43
 */
public class NodeVersion {
    private String number;
    private String lucene_version;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLucene_version() {
        return lucene_version;
    }

    public void setLucene_version(String lucene_version) {
        this.lucene_version = lucene_version;
    }
}
