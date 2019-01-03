package com.example.elasticsearch.annotation;

import com.example.elasticsearch.config.ElasticsearchConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动配置自定义的Elasticsearch
 *
 * @author tanzhi
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ElasticsearchConfig.class)
public @interface EnableMyElasticsearch {
}
