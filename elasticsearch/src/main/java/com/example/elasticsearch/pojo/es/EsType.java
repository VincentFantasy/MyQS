package com.example.elasticsearch.pojo.es;

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
