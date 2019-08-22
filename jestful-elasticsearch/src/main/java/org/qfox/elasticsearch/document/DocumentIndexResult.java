package org.qfox.elasticsearch.document;

/**
 * 文档索引结果
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 14:28
 */
public class DocumentIndexResult {
    private String _index;
    private String _type;
    private String _id;
    private Integer _version;
    private boolean created;

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

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
