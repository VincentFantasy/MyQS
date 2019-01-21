package com.example.myes.constant;

/**
 * 文档属性的类型
 *
 * @author tanzhi
 * @version 1.0
 */
public enum  EsFiledsTypeConstant {

    /**
     * 字符串
     */
    STRING("string"),

    /**
     * 字节
     */
    BYTE("byte"),

    /**
     * 整型
     */
    SHORT("short"),
    INTEGER("integer"),
    LONG("long"),

    /**
     * 浮点
     */
    FLOAT("float"),
    DOUBLE("doubke"),

    /**
     * 布尔
     */
    BOOLEAN("boolean"),

    /**
     * 日期和时间
     */
    DATE("date"),

    /**
     * 二进制类型
     */
    BINARY("binary"),

    /**
     * ipv4地址
     */
    IP("ip"),

    /**
     * 用于存储索引的字数信息
     */
    TOKEN_COUNT("token_count");

    private final String value;

    EsFiledsTypeConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
