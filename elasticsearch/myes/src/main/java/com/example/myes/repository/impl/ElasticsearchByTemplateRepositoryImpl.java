package com.example.myes.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myes.pojo.es.EsResult;
import com.example.myes.util.EsUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.example.myes.pojo.es.EsData;
import com.example.myes.pojo.es.EsQuery;
import com.example.myes.pojo.es.EsType;
import com.example.myes.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;

/**
 *
 * @author tanzhi
 */
public class ElasticsearchByTemplateRepositoryImpl implements ElasticsearchRepository {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private ElasticsearchTemplate elasticsearchTemplate;

	private ResultsExtractor<EsResult> resultResultsExtractor;

	/**
	 * 创建ElasticsearchByTemplateRepositoryImpl时创建处理结果类
	 */
	public ElasticsearchByTemplateRepositoryImpl() {

		resultResultsExtractor = getResultResultsExtractor();
	}

	/**
	 * 与spring整合
	 *
	 * @param elasticsearchTemplate
	 *            elasticsearchTemplate
	 */
	public ElasticsearchByTemplateRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {

		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * 没有实现全部操作的封装，可以获取template自定义开发
	 *
	 * @return elasticsearchTemplate
	 */
	public ElasticsearchTemplate getElasticsearchTemplate() {

		return elasticsearchTemplate;
	}

	@Override
	public boolean hasIndex(String index) {

		return thisHasIndex(index);
	}

	@Override
	public boolean createIndex(String index) {

		if (thisHasIndex(index)) {
			throw new RuntimeException("该索引已存在:" + index);
		}

		return elasticsearchTemplate.createIndex(index);
	}

	@Override
	public boolean deleteIndex(String index) {

		if (!thisHasIndex(index)) {
			throw new RuntimeException("该索引不存在:" + index);
		}

		return elasticsearchTemplate.deleteIndex(index);
	}

	@Override
	public boolean createType(EsType type) throws IOException {

		if (type == null) {
			throw new RuntimeException("type不能为空");
		}

		if (!thisHasIndex(type.getIndex())) {
			throw new RuntimeException("该索引不存在:" + type.getIndex());
		}

		if (StringUtils.isEmpty(type.getType())) {
			throw new RuntimeException("文档类型不能为空");
		}

		if (type.getFields().size() == 0) {
			throw new RuntimeException("需要为文档类型添加字段");
		}

		return elasticsearchTemplate.putMapping(type.getIndex(), type.getType(),
				EsUtil.getMapping(type.getFields()));
	}

	@Override
	public String insertData(EsData data) {

		if (data == null) {
			throw new RuntimeException("data不能为空");
		}

		if (!thisHasIndex(data.getIndex())) {
			throw new RuntimeException("该索引不存在:" + data.getIndex());
		}

		if (StringUtils.isEmpty(data.getType())) {
			throw new RuntimeException("文档类型不能为空");
		}

		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setIndexName(data.getIndex());
		indexQuery.setType(data.getType());
		try {
			indexQuery.setSource(EsUtil.map2XBString(data.getData()));
		} catch (IOException e) {
			log.error("insertData:", e);
			e.printStackTrace();
		}

		return elasticsearchTemplate.index(indexQuery);
	}

	@Override
	public boolean updateData(EsData data) {

		if (data == null) {
			throw new RuntimeException("data不能为空");
		}

		if (!thisHasIndex(data.getIndex())) {
			throw new RuntimeException("该索引不存在:" + data.getIndex());
		}

		if (StringUtils.isEmpty(data.getType())) {
			throw new RuntimeException("文档类型不能为空");
		}

		if (StringUtils.isEmpty(data.getId())) {
			throw new RuntimeException("文档id不能为空");
		}

		UpdateQuery updateQuery = new UpdateQuery();
		UpdateRequest request = new UpdateRequest();
		updateQuery.setIndexName(data.getIndex());
		updateQuery.setType(data.getType());
		updateQuery.setId(data.getId());
		request.doc(data.getData());
		updateQuery.setUpdateRequest(request);

		UpdateResponse response = elasticsearchTemplate.update(updateQuery);
		log.debug("version:" + response.getVersion());

		return !response.isCreated();
	}

	@Override
	public boolean deleteData(EsData data) {

		if (data == null) {
			throw new RuntimeException("data不能为空");
		}

		if (!thisHasIndex(data.getIndex())) {
			throw new RuntimeException("该索引不存在:" + data.getIndex());
		}

		if (StringUtils.isEmpty(data.getType())) {
			throw new RuntimeException("文档类型不能为空");
		}

		if (StringUtils.isEmpty(data.getId())) {
			throw new RuntimeException("文档id不能为空");
		}

		return elasticsearchTemplate.delete(data.getIndex(), data.getType(), data.getId()) != null;
	}

	@Override
	public EsResult matchAllQuery(EsQuery esQuery) {

		return searchFunction(QueryBuilders.matchAllQuery(), esQuery);
	}

	@Override
	public EsResult matchQuery(String field, String keyword) {

		if (StringUtils.isEmpty(field)) {
			throw new RuntimeException("搜索字段不能为空");
		}

		if (StringUtils.isEmpty(keyword)) {
			throw new RuntimeException("搜索词不能为空");
		}

		QueryBuilder queryBuilder = QueryBuilders.matchQuery(field, keyword);
		return searchFunction(queryBuilder, null);
	}

	@Override
	public EsResult matchMultiQuery(String[] field, String keyword) {

		if (field == null || field.length == 0) {
			throw new RuntimeException("搜索字段不能为空");
		}

		if (StringUtils.isEmpty(keyword)) {
			throw new RuntimeException("搜索词不能为空");
		}

		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, keyword);
		return searchFunction(queryBuilder, null);
	}

	@Override
	public EsResult boolQuery(BoolQueryBuilder boolQueryBuilder, EsQuery esQuery) {

		return searchFunction(boolQueryBuilder, esQuery);
	}

	@Override
	public EsResult queryString(EsQuery esQuery) {

		if (esQuery == null || StringUtils.isEmpty(esQuery.getKeyword())) {
			throw new RuntimeException("搜索词不能为空");
		}

		// 通配符查询
		QueryBuilder queryBuilder = QueryBuilders
				.queryStringQuery("*" + esQuery.getKeyword() + "*");
		return searchFunction(queryBuilder, esQuery);
	}

	@Override
	public EsResult termQuery(EsQuery esQuery) {

		if (esQuery == null) {
			throw new RuntimeException("data不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getField())) {
			throw new RuntimeException("搜索字段不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getKeyword())) {
			throw new RuntimeException("搜索词不能为空");
		}

		QueryBuilder queryBuilder = QueryBuilders.termQuery(esQuery.getField(),
				esQuery.getKeyword());
		return searchFunction(queryBuilder, esQuery);
	}

	@Override
	public EsResult fuzzyQuery(EsQuery esQuery) {

		if (esQuery == null) {
			throw new RuntimeException("data不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getField())) {
			throw new RuntimeException("搜索字段不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getKeyword())) {
			throw new RuntimeException("搜索词不能为空");
		}

		QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery(esQuery.getField(),
				esQuery.getKeyword());
		return searchFunction(queryBuilder, esQuery);
	}

	@Override
	public EsResult wildcardQuery(EsQuery esQuery) {

		if (esQuery == null) {
			throw new RuntimeException("data不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getField())) {
			throw new RuntimeException("搜索字段不能为空");
		}

		if (StringUtils.isEmpty(esQuery.getKeyword())) {
			throw new RuntimeException("搜索词不能为空");
		}

		QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(esQuery.getField(),
				esQuery.getKeyword());
		return searchFunction(queryBuilder, esQuery);
	}

	@Override
	public EsResult searchScrollFunction(EsQuery esQuery) {

		if (esQuery == null) {
			throw new RuntimeException("data不能为空");
		}

		if (!thisHasIndex(esQuery.getIndex())) {
			throw new RuntimeException("该索引不存在:" + esQuery.getIndex());
		}

		if (StringUtils.isEmpty(esQuery.getType())) {
			throw new RuntimeException("文档类型不能为空");
		}

		return searchFunction(QueryBuilders.matchAllQuery(), esQuery);
	}

	@Override
	public EsResult searchByScrollId(String scrollId) {

		// 再次发送请求,并使用上次搜索结果的ScrollId
		SearchResponse searchResponse = elasticsearchTemplate.getClient()
				.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000)).execute()
				.actionGet();

		return resultResultsExtractor.extract(searchResponse);
	}

	@Override
	public boolean clearScroll(String scrollId) {

		elasticsearchTemplate.clearScroll(scrollId);
		return true;
	}

	@Override
	public boolean clearScroll(List<String> scrollIdList) {

		scrollIdList.forEach(id -> elasticsearchTemplate.clearScroll(id));
		return true;
	}

	/**
	 * 搜索查询
	 *
	 * @param queryBuilder
	 *            查询条件
	 * @return 结果
	 */
	private EsResult searchFunction(QueryBuilder queryBuilder, EsQuery esQuery) {

		EsResult esResult = new EsResult();

		NativeSearchQuery query = new NativeSearchQuery(queryBuilder);

		if (esQuery != null) {
			// 索引与文档类型
			if (!StringUtils.isEmpty(esQuery.getIndex())) {
				query.addIndices(esQuery.getIndex());
				if (!StringUtils.isEmpty(esQuery.getType())) {
					query.addTypes(esQuery.getType());
				}
			}
			// 搜索方式
			if (esQuery.getSearchType() == null) {
				query.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			} else {
				query.setSearchType(esQuery.getSearchType());
			}
			// 分页
			if (esQuery.getPageNo() != null && esQuery.getPageSize() != null) {
				esResult.setPageNo(esQuery.getPageNo() - 1);
				esResult.setPageSize(esQuery.getPageSize());
				query.setPageable(new PageRequest(esQuery.getPageNo() - 1, esQuery.getPageSize()));
			} else {
				// 默认还是要分页，查前50条
				esResult.setPageNo(0);
				esResult.setPageSize(50);
				query.setPageable(new PageRequest(0, 50));
			}
			// 排序
			if (esQuery.getSortFiled() != null && esQuery.getSort() != null) {
				if (StringUtils.equalsIgnoreCase(esQuery.getSort().toString(), "desc")) {
					query.addSort(
							new Sort(new Sort.Order(Sort.Direction.DESC, esQuery.getSortFiled())));
				} else {
					query.addSort(
							new Sort(new Sort.Order(Sort.Direction.ASC, esQuery.getSortFiled())));
				}
			}
		}

		return elasticsearchTemplate.query(query, resultResultsExtractor);
	}

	/**
	 * 检验索引是否存在
	 *
	 * @param index
	 *            索引
	 * @return 是否存在
	 */
	private boolean thisHasIndex(String index) {

		if (StringUtils.isEmpty(index)) {
			throw new RuntimeException("索引名不能为空");
		}

		return elasticsearchTemplate.indexExists(index);
	}

	/**
	 * 改成了函数式实现接口
	 *
	 * @return ResultsExtractor实现类
	 */
	private ResultsExtractor<EsResult> getResultResultsExtractor() {
		return (response) -> {
			EsResult esResult = new EsResult();
			SearchHits searchHits = response.getHits();

			List<Map<String, Object>> resultList = null;

			// log.debug("共匹配到:" + searchHits.getTotalHits() + "条记录!");
			long total = searchHits.getTotalHits();
			if (total > 0) {
				esResult.setCount(total);
				resultList = new ArrayList<>((int) total);


				for (SearchHit searchHit : searchHits) {
					Map<String, Object> source = searchHit.getSource();
					// 多加入一些关键信息
					Map<String, Object> map = new HashMap<>(source.size() + 5);
					map.putAll(source);
					map.put("id", searchHit.getId());
					map.put("index", searchHit.getIndex());
					map.put("type", searchHit.getType());
					map.put("version", searchHit.getVersion());
					map.put("shard", searchHit.getShard());

					resultList.add(map);
				}
			}

			esResult.setResluts(resultList).setScrollId(response.getScrollId());

			return esResult;
		};
	}
}
