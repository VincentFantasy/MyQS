package com.example.elasticsearch.pojo;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品与属性ES实体类
 *
 * @author tanzhi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsattrindexPojo {

	private String attrId;

	/**
	 * 属性值ID
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "attr_id")
	private String attrvalueId;
}