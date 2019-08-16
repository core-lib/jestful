package org.qfox.elasticsearch.node;

/**
 * 集群的文档数量查询结果
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:54
 */
public class NodeCountResult {
    private long count;
    private Shards _shards;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Shards get_shards() {
        return _shards;
    }

    public void set_shards(Shards _shards) {
        this._shards = _shards;
    }

    /**
     * 节点分片
     *
     * @author Payne 646742615@qq.com
     * 2019/8/16 17:55
     */
    public static class Shards {
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
}
