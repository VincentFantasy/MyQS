package com.example.myes.util;

import com.example.myes.pojo.es.EsField;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * es工具类
 *
 * @author tanzhi
 *
 */
public class EsUtil {

    /**
     * source可以使用Object..或Map参数，但都是后面都是转换成XContentBuilder再使用
     *
     * @param fieldList
     *            字段及字段属性
     * @return 构成的XContentBuilder
     */
    public static XContentBuilder getMapping(List<EsField> fieldList) throws IOException {

        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                .startObject("properties");

        for (EsField field : fieldList) {
            mapping.startObject(field.getName());

            if (field.getType() != null) {
                mapping.field("type", field.getType().getValue());
            }
            if (field.getIndex() != null) {
                mapping.field("index", field.getIndex().getValue());
            }
            if (StringUtils.isEmpty(field.getAnalyzer())) {
                mapping.field("analyzer", field.getAnalyzer());
            }
            if (StringUtils.isEmpty(field.getSearchAnalyzer())) {
                mapping.field("searchAnalyzer", field.getSearchAnalyzer());
            }
            for (Map.Entry<String, String> entry : field.getOther().entrySet()) {
                if (StringUtils.isEmpty(entry.getValue())) {
                    mapping.field(entry.getKey(), entry.getValue());
                }
            }

            mapping.endObject();
        }
        mapping.endObject().endObject();

        return mapping;
    }

    /**
     * 把map对象转成 XContentBuilder的utf-8编码的字符串
     *
     * @param map map对象
     * @return XContentBuilder的utf-8编码的字符串
     * @throws IOException IOException
     */
    public static String map2XBString(Map map) throws IOException {
        return XContentFactory.jsonBuilder().map(map).bytes().toUtf8();
    }

}
