package com.example.elasticsearch.constant;

/**
 * 文档属性的index，控制该属性控制字段是否编入索引被搜索
 *
 * @author tanzhi
 * @version 1.0
 */
public enum EsFiledsIndexConstant {

    /**
     * 表示该字段被分析，编入索引，产生的token能被搜索到；默认值
     */
    ANALYZED("analyzed"),

    /**
     * 表示该字段不会被分析，使用原始值编入索引，在索引中作为单个词；
     */
    NOT_ANALYZED("not_analyzed"),

    /**
     * 不编入索引，无法搜索该字段
     */
    NO("no");

    private final String value;

    EsFiledsIndexConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
