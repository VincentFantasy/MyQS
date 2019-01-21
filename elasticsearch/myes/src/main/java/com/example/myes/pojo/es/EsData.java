package com.example.myes.pojo.es;

import java.util.Map;

/**
 * 数据用
 *
 * @author tanzhi
 */
public class EsData {

    /**
     * 索引名
     */
    private String index;

    /**
     * 文档类型名
     */
    private String type;

    /**
     * 文档的id，用于更新
     */
    private String id;

    /**
     * 数据对象。。
     * 可以是实现map的类
     * 包括jsonObject
     *
     * source方法
     * 可以是obj, obj.1是key,obj.0是value
     * 可以是编码好的string，BytesReference对象的utf8编码
     * 或者实现map接口的类
     */
    private Map data;

    public EsData() {
    }

    public EsData(String index, String type, String id) {
        this.index = index;
        this.type = type;
        this.id = id;
    }

    public EsData(String index, String type, Map json) {
        this.index = index;
        this.type = type;
        this.data = json;
    }

    public EsData(String index, String type, String id, Map data) {
        this.index = index;
        this.type = type;
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EsData{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", data=" + data +
                '}';
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

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
