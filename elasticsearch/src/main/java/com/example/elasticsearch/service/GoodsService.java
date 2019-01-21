package com.example.elasticsearch.service;

import com.example.elasticsearch.esentity.DemoGoods;
import com.example.elasticsearch.esentity.EsGoods;
import com.example.elasticsearch.pojo.GoodsByRefIdQueryVo;
import com.example.elasticsearch.pojo.StoreIdQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Description: 商品管理业务层接口
 * @Author wangpeng1
 * @Date 2018/11/5 9:50
 */
public interface GoodsService {

	void importOne(String goodId);

	/**
	 * 商品数据导入elasticsearch
	 *
	 */
	void importAll();

	/**
	 * 根据id查询
	 *
	 * @param id 商品id
	 * @return 查询到的数据
	 */
	DemoGoods findByGoodsId(String id);

	/**
	 * 根据商品名搜索
	 *
	 * @param name 商品名，可以是分词
	 * @return 搜索到的商品
	 */
	Page<DemoGoods> searchByName(String name, int pageNo, int pageSize);

	/**
	 * 普通搜索查询
	 * 可以动态分页、排序等
	 *
	 * @param goodsIndexQueryVo
	 * @return
	 */
	//Page<DemoGoods> queryIndex(GoodsIndexQueryVo goodsIndexQueryVo);

	/**
	 * 搜索范围内的商品
	 *
	 * @param lat 纬度
	 * @param lon 经度
	 * @param km 多少km内的
	 * @return 结果
	 */
	Page<DemoGoods> queryLocationRange(double lat, double lon, int km);

	/**
	 * 根据电子菜谱ID，目标经纬度，搜索范围和分类ID查询电子菜谱对应的点餐单
	 *
	 * @param goodsByRefIdQueryVo 相关查询数据
	 * @return 结果列表
	 */
	Page<EsGoods> queryGoodsByRef(GoodsByRefIdQueryVo goodsByRefIdQueryVo);

	/**
	 * 查询店铺的商品列表（不分页，不传storeId时查本店铺，传storeId时查相关店铺）
	 *
	 * @param storeIdQueryVo
	 * @return
	 */
	Page<EsGoods> queryGoodsByStore(StoreIdQueryVo storeIdQueryVo, String storeId);

}
