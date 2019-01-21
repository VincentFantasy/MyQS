package com.example.elasticsearch;

import com.example.elasticsearch.esdao.DemoGoodsRepository;
import com.example.elasticsearch.esentity.DemoGoods;
import com.example.elasticsearch.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchRepositoryTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void importOne() {
        goodsService.importOne("219989121262239745");
    }

    @Test
    public void importAll() {
        goodsService.importAll();
    }

    @Test
    public void findOne() {
        System.out.println(goodsService.findByGoodsId("219989121262239745"));
    }

    @Test
    public void findByName() {
        Page<DemoGoods> goods = goodsService.searchByName("口水鸡", 1, 10);
        System.out.println(goods.getTotalElements());
        System.out.println(goods.getTotalPages());
        System.out.println(goods.getContent().toString());
    }

    @Autowired
    private DemoGoodsRepository repository;

    @Test
    public void updatePonit() {
        DemoGoods demoGoods = repository.findOne("AWgl6FnpM7W8kUBC4GrH");
        demoGoods.setLocation(new GeoPoint(22.682038, 114.168422));
        repository.save(demoGoods);
    }

    @Test
    public void getNaer() {
        Page<DemoGoods> goods = goodsService.queryLocationRange(22.719209, 114.170057, 5);
        System.out.println(goods.getTotalElements());
        System.out.println(goods.getTotalPages());
        System.out.println(goods.getContent().toString());
    }

}
