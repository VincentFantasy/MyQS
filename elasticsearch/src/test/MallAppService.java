package com.example.elasticsearch.service;

import com.example.elasticsearch.entity.MallApp;

import java.util.List;

/**
 * 测试Spring封装ES的API
 * 
 * @author wangpeng1
 * @since 2018年11月29日
 */
public interface MallAppService {

	List<MallApp> findAll();

}
