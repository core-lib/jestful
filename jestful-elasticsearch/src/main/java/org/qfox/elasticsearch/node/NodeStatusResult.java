package org.qfox.elasticsearch.node;

/**
 * 节点状态查询结果
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:41
 */
public class NodeStatusResult {
    private int status;
    private String name;
    private Version version;
    private String tagline;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     * 节点版本
     *
     * @author Payne 646742615@qq.com
     * 2019/8/16 17:43
     */
    public static class Version {
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
}
