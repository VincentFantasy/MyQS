package com.example.elasticsearch.esdao;

import com.example.elasticsearch.esentity.DemoGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 
 * @author wangpeng1
 * @since 2018年10月23日
 */
public interface DemoGoodsRepository extends ElasticsearchRepository<DemoGoods, String> {

	DemoGoods findByGoodsId(String id);

	Page<DemoGoods> findAllByGoodsName(String goodsName, Pageable pageable);
}
