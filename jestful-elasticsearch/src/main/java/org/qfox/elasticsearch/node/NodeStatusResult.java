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
    private NodeVersion version;
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

    public NodeVersion getVersion() {
        return version;
    }

    public void setVersion(NodeVersion version) {
        this.version = version;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
}
