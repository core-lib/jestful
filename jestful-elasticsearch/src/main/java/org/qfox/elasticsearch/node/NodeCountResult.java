package org.qfox.elasticsearch.node;

import org.qfox.elasticsearch.Shards;

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


}
