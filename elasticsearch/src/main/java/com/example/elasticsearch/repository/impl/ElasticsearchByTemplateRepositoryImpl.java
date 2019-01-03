package com.example.elasticsearch.repository.impl;

import com.example.elasticsearch.pojo.es.EsData;
import com.example.elasticsearch.pojo.es.EsQuery;
import com.example.elasticsearch.pojo.es.EsType;
import com.example.elasticsearch.repository.ElasticsearchRepository;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticsearchByTemplateRepositoryImpl implements ElasticsearchRepository {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private ElasticsearchTemplate elasticsearchTemplate;

	/**
	 * 与spring整合
	 *
	 * @param elasticsearchTemplate
	 *            elasticsearchTemplate
	 */
	public ElasticsearchByTemplateRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {

		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	@Override
	public boolean hasIndex(String index) {

		return false;
	}

	@Override
	public boolean createIndex(String index) {

		return false;
	}

	@Override
	public boolean deleteIndex(String index) {

		return false;
	}

	@Override
	public boolean createType(EsType type) throws IOException {

		return false;
	}

	@Override
	public boolean insertData(EsData data) {

		return false;
	}

	@Override
	public boolean updateData(EsData data) throws ExecutionException, InterruptedException {

		return false;
	}

	@Override
	public boolean deleteData(EsData data) {

		return false;
	}

	@Override
	public List<Map<String, Object>> matchAllQuery(SearchType searchType, EsQuery esQuery) {

		return null;
	}

	@Override
	public List<Map<String, Object>> matchQuery(String field, String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> matchMultiQuery(String[] field, String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> boolQuery(BoolQueryBuilder boolQueryBuilder, int pageno,
			int pagesize, SortBuilder sortBuilder) {

		return null;
	}

	@Override
	public List<Map<String, Object>> queryString(String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> termQuery(String field, String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> fuzzyQuery(String field, String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> wildcardQuery(String field, String keyword) {

		return null;
	}

	@Override
	public List<Map<String, Object>> searchScrollFunction(EsQuery query) {

		return null;
	}

	@Override
	public List<Map<String, Object>> searchByScrollId(String scrollId) {

		return null;
	}

	@Override
	public boolean clearScroll(String scrollId) {

		return false;
	}

	@Override
	public boolean clearScroll(List<String> scrollIdList) {

		return false;
	}

	/**
     * 没有实现全部操作的封装，可以获取template自定义开发
     *
     * @return elasticsearchTemplate
     */
     public ElasticsearchTemplate getElasticsearchTemplate() {
         return elasticsearchTemplate;
     }

}
