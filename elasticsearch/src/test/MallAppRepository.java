package com.example.elasticsearch.dao;

import com.example.elasticsearch.entity.MallApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MallAppRepository extends ElasticsearchRepository<MallApp, String> {

}
