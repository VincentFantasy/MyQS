package com.example.elasticsearch.esdao;

import com.example.elasticsearch.esentity.EsGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Goods
 *
 * @author tanzhi
 */
public interface EsGoodsRepository extends ElasticsearchRepository<EsGoods, String> {

	/**
	 * 根据商品查询
	 * 查询单个商品用
	 *
	 * @param goodsId 商品id
	 * @return EsGoods
	 */
	EsGoods findByGoodsId(String goodsId);

	/**
	 * 根据商品id删除某个商品ES
	 * 删除单个商品
	 *
	 * @param goodsId 商品id
	 */
	void deleteByGoodsId(String goodsId);

	/**
	 * 根据商品名字搜索商品
	 * 根据关键字搜索商品
	 *
	 * @param goodsName 商品名或分词
	 * @param pageable 分页
	 * @return page 商品es
	 */
	Page<EsGoods> findAllByGoodsName(String goodsName, Pageable pageable);
}
