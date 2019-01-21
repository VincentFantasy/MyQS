package com.example.elasticsearch.pojo;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品图片ES实体类
 *
 * @author tanzhi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsimagesPojo {
	/**
	 * 图片id
	 */
	private String id;

	/**
	 * 商品图片
	 */
	private String imagePath;

	/**
	 * 商品图片排序
	 */
	private Integer sort;

	/**
	 * 商品图片默认主图，1是，0否
	 */
	private Integer isDefault;

	/**
	 * 文件类型：1图片2视频
	 */
	private Integer fileType;
}