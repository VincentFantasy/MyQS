package com.example.myes.pojo.es;

import java.util.ArrayList;
import java.util.List;

/**
 * 索引文档类型
 *
 * @author tanzhi
 *
 */
public final class EsType {

    /**
     * 索引名
     */
    private String index;

    /**
     * 文档类型名
     */
    private String type;

    /**
     * 文档的属性
     * 这里用list可以保证顺序？而不是使用map，而且map还是要被构造成XContentBuilder，并没有减少开销
     */
    private List<EsField> fields = new ArrayList<>();

    public EsType() {
    }

    public EsType(String index, String type, List<EsField> fields) {
        this.index = index;
        this.type = type;
        this.fields = fields;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EsField> getFields() {
        return fields;
    }

    public void setFields(List<EsField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "EsType{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", fields=" + fields +
                '}';
    }
}
