package com.example.elasticsearch.esentity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.elasticsearch.pojo.GoodsattrindexPojo;
import com.example.elasticsearch.pojo.GoodsimagesPojo;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wangpeng1
 * @since 2018年10月23日
 *
 * 只把goods部分信息取了出来
 *
 * @author tanzhi
 * @since 2019/1/4
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "demo", type = "goods")
public class EsGoods {

	/**
	 * 商品id（ES）
	 */
	@Id
	@JSONField(name = "id")
	private String id;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@Parent(type = "store")
	private String storeEsId;

	/**
	 * 商品id
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "goods_id")
	private String goodsId;

	/**
	 * 应用ID
	 * 原Good表 appId
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "good_app_id")
	private String goodAppId;

	/**
	 * 店铺id
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "store_id")
	private String goodStoreId;

	/**
	 * 品牌id
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "brand_id")
	private String brandId = "0";

	/**
     * 商品分类id
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "gc_id")
	private String gcId;

	/**
	 * 类型id
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "type_id")
	private String typeId = "0";

	/**
	 * 商品编号
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "goods_serial")
	private String goodsSerial;

	/**
	 * 应用关联商品ID
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "ref_id")
	private String refId;

	/**
	 * 商品名称
	 */
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.String)
	@JSONField(name = "goods_name")
	private String goodsName;

	/**
	 * 商品广告词
	 */
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.String)
	@JSONField(name = "goods_advword")
	private String goodsAdvword;

	/**
	 * 商品描述
	 */
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.String)
	@JSONField(name = "goods_desc")
	private String goodsDesc;

	/**
	 * 商品价格
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_price")
	private BigDecimal goodsPrice;

	/**
	 * 商品市场价
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_marketprice")
	private BigDecimal goodsMarketprice;

	/**
	 * 商品成本价
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_costprice")
	private BigDecimal goodsCostprice;

	/**
	 * 商品折扣
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_discount")
	private BigDecimal goodsDiscount;

	/**
	 * 商品推荐 1:是 0:否
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Integer)
	@JSONField(name = "goods_commend")
	private Integer goodsCommend = 0;

	/**
	 * 商品运费 0为免运费
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "goods_freight")
	private BigDecimal goodsFreight;

	/**
	 * 商品是否开具增值税发票 1:是 0:否
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Integer)
	@JSONField(name = "goods_vat")
	private Integer goodsVat = 0;
	/**
	 * 是否为虚拟商品 1:是 0:否
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Integer)
	@JSONField(name = "is_virtual")
	private Integer isVirtual = 0;

	/**
	 * 总库存
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Integer)
	@JSONField(name = "goods_storage")
	private Integer goodsStorage = 0;

	/**
	 * 商品分类完整路径冗余，方便按分类或子分类查询商品
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "gc_fullpath")
	private String gcFullpath;
	/**
	 * 最低价
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "price_min")
	private BigDecimal priceMin;

	/**
	 * 最高价
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Double)
	@JSONField(name = "price_max")
	private BigDecimal priceMax;

	/**
	 * 销量
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Long)
	@JSONField(name = "goods_salenum")
	private Long goodsSalenum = 0L;

	/**
	 * 商品默认图片
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "image_path")
	private String imagePath;

	/**
	 * 评价数
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Long)
	@JSONField(name = "evaluation_count")
	private Long evaluationCount = 0L;

	/**
	 * 扩展信息JSON
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "extend_info")
	private String extendInfo;

	/**
	 * 规格值
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "spec_value")
	private String specValue;

	/**
	 * 商品状态 0:下架 1:正常 10:违规（禁售）2:编辑
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
	@JSONField(name = "goods_state")
	private String goodsState = "2";

	/**
	 * 商品添加时间
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Date)
	@JSONField(name = "goods_addtime")
	private Timestamp goodsAddtime;

	/**
	 * 上架时间
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Date)
	@JSONField(name = "goods_shelftime")
	private Timestamp goodsShelftime;

	/**
	 * 图片
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Object)
	private List<GoodsimagesPojo> goodsimagesPojos = new ArrayList<>();

	/**
	 * 属性
	 */
	@Field(index = FieldIndex.not_analyzed, type = FieldType.Object)
	private List<GoodsattrindexPojo> goodsattrindexes = new ArrayList<>();
}
