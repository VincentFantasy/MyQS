package com.example.elasticsearch.esentity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

/**
 * 
 * @author wangpeng1
 * @since 2018年10月23日
 *
 * @author tanzhi
 * @since 2019/1/4
 *
 */
// indexName索引名称 可以理解为数据库名 必须为小写
// 不然会报org.elasticsearch.indices.InvalidIndexNameException异常
// type类型 可以理解为表名
// @Document(indexName = "demo", type = "goods", shards = 1, replicas = 0,
// refreshInterval = "-1")
@Data
@Document(indexName = "demo", type = "goods")
public class DemoGoods {

	/**
	 * @Id 指定es内存储id
	 * @JSONfield 指定别名
	 */
	@Id
	@JSONField(name = "id")
	private String id;

	/**
	 * @Field index默认分析，不分析的属性要加FieldIndex.not_analyzed， type类型默认自动识别
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "goods_id")
	private String goodsId;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "app_id")
	private String appId;

	@Field(index = FieldIndex.analyzed, type = FieldType.String, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	@JSONField(name = "goods_name")
	private String goodsName;

	@Field(index = FieldIndex.analyzed, type = FieldType.String, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	@JSONField(name = "goods_desc")
	private String goodsDesc;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_price")
	private Double goodsPrice;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "shop_id")
	private String shopId;

	/**
	 * 需要spring data Elasticsearch 下的，地位位置注解
	 */
	@GeoPointField
	private GeoPoint location;
}
