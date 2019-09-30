package org.qfox.elasticsearch.document;

/**
 * 文档查询结果
 *
 * @author Payne 646742615@qq.com
 * 2019/9/30 14:14
 */
public class DocumentQueryResult<T> extends DocumentBasicResult {
    private boolean found;
    private T _source;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public T get_source() {
        return _source;
    }

    public void set_source(T _source) {
        this._source = _source;
    }
}
