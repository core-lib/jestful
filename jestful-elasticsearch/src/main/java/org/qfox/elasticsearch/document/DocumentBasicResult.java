package org.qfox.elasticsearch.document;

import org.qfox.elasticsearch.Shards;

/**
 * 文档操作基础结果
 *
 * @author Payne 646742615@qq.com
 * 2019/9/30 14:12
 */
public class DocumentBasicResult {
    private String _index;
    private String _type;
    private String _id;
    private Integer _version;
    private String result;
    private Shards _shards;
    private Integer _seq_no;
    private Integer _primary_term;

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer get_version() {
        return _version;
    }

    public void set_version(Integer _version) {
        this._version = _version;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Shards get_shards() {
        return _shards;
    }

    public void set_shards(Shards _shards) {
        this._shards = _shards;
    }

    public Integer get_seq_no() {
        return _seq_no;
    }

    public void set_seq_no(Integer _seq_no) {
        this._seq_no = _seq_no;
    }

    public Integer get_primary_term() {
        return _primary_term;
    }

    public void set_primary_term(Integer _primary_term) {
        this._primary_term = _primary_term;
    }
}
