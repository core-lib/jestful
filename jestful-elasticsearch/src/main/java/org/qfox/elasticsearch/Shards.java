package org.qfox.elasticsearch;

/**
 * 节点分片
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:55
 */
public class Shards {
    private int total;
    private int successful;
    private int failed;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccessful() {
        return successful;
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
}