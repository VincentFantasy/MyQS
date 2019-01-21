package com.example.myes.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.example.myes.config.ElasticsearchConfig;

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
