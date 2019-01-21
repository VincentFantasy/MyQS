package com.example.myes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.example.myes.repository.ElasticsearchRepository;
import com.example.myes.repository.impl.ElasticsearchRepositoryImpl;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * client可以配置文件配置，或者自己另外注册
 *
 * 两种使用方式，
 * 一种直接使用elasticsearchRepository，实际上是使用elasticsearchTemplate-》client
 * 另一种是使用repository继承ElasticsearchRepository进行操作,
 *      使用spring.data.elasticsearch.repositories.enabled=true 在配置文件开启
 *      或者使用EnableElasticsearchRepositories注解开启支持
 *
 * @author tanzhi
 *
 * //@EnableElasticsearchRepositories(basePackages = "com.example.elasticsearch.repository")
 */
@Configuration
public class ElasticsearchConfig {

    /**
     * 在配置类注册bean
     *
     * @return elasticsearchRepository
     */
    @Bean("elasticsearchRepository")
    public ElasticsearchRepository elasticsearchRepository(ElasticsearchTemplate elasticsearchTemplate) {
        return new ElasticsearchRepositoryImpl(elasticsearchTemplate.getClient());
    }
}
