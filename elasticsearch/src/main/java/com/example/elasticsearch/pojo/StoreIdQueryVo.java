package com.example.elasticsearch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tanzhi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreIdQueryVo {

	/**
	 * 店铺ID
	 */
	private String storeId;

	/**
	 * 商品名称或商品广告词
	 */
	private String goodsName;

	/**
	 * 商品分类ID
	 */
	private String gcId;

	/**
	 * 排序字段
	 */
	private String order;

	/**
	 * 排序规则，升序或降序
	 */
	private String sort;
}