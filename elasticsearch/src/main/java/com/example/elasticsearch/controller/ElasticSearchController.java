package com.example.elasticsearch.controller;

import com.example.elasticsearch.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author wangpeng1
 * @since 2018年10月23日
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

	@Autowired
	private GoodsService goodsService;


}
