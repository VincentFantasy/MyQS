package com.example.elasticsearch.esdao;

import java.util.List;

import com.example.elasticsearch.esentity.ESStore;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * 
 * @author tanzhi
 * @since 2019/1/9
 */
public interface EsStoreRepository extends ElasticsearchRepository<ESStore, String> {

    /**
     * 根据商店id列表搜索店铺es
     * 电子菜单查询用
     *
     * @param storeIds 店铺id
     * @return 店铺es list
     */
    List<ESStore> findAllByIdIn(List<String> storeIds);
}
