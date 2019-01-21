package com.example.myes.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.myes.pojo.es.*;
import com.example.myes.util.EsUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.myes.repository.ElasticsearchRepository;

/**
 *
 * @author tanzhi
 *
 */
public class ElasticsearchRepositoryImpl implements ElasticsearchRepository {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private Client client;

	/**
	 * 可以不使用spring，但是部分功能不能使用
	 * 
	 * @param client
	 *            Elasticsearch client
	 */
	public ElasticsearchRepositoryImpl(Client client) {

		this.client = client;
	}

	@Override
	public boolean hasIndex(String index) {

		if (StringUtils.isEmpty(index)) {
			throw new RuntimeException("索引名不能为空");
		}

		return thisHasIndex(index);
	}

	@Override
	public boolean createIndex(String index) {

		if (thisHasIndex(index)) {
			throw new RuntimeException("该索引已存在:" + index);
		}

		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
				.prepareCreate(index);
		CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
		// log.info(createIndexResponse.getContext().toString());

		return response.isAcknowledged();
	}

	@Override
	public boolean deleteIndex(String index) {

		if (!thisHasIndex(index)) {
			throw new RuntimeException("该索引不存在:" + index);
		}

		DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices()
				.prepareDelete(index);
		DeleteIndexResponse response = deleteIndexRequestBuilder.get();

		return response.isAcknowledged();
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

		PutMappingRequest mapping = Requests.putMappingRequest(type.getIndex()).type(type.getType())
				.source(EsUtil.getMapping(type.getFields()));
		PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping)
				.actionGet();

		return putMappingResponse.isAcknowledged();
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

		IndexResponse response = client.prepareIndex(data.getIndex(), data.getType())
				.setSource(data.getData()).get();
		log.debug("version:" + response.getVersion());

		return response.getId();
	}

	@Override
	public boolean updateData(EsData data) throws ExecutionException, InterruptedException {

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

		UpdateRequest request = new UpdateRequest();
		request.index(data.getIndex());
		request.type(data.getType());
		request.id(data.getId());
		request.doc(data.getData());

		UpdateResponse response = client.update(request).get();
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

		DeleteResponse response = client
				.prepareDelete(data.getIndex(), data.getType(), data.getId()).get();
		log.debug("version:" + response.getVersion());

		return response.isFound();
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

		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, field);
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
		SearchResponse searchResponse = client.prepareSearchScroll(scrollId)
				.setScroll(new TimeValue(60000)).execute().actionGet();

		return getResult(searchResponse, new EsResult());
	}

	@Override
	public boolean clearScroll(String scrollId) {

		ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
		clearScrollRequestBuilder.addScrollId(scrollId);
		ClearScrollResponse response = clearScrollRequestBuilder.get();
		return response.isSucceeded();
	}

	@Override
	public boolean clearScroll(List<String> scrollIdList) {

		ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
		clearScrollRequestBuilder.setScrollIds(scrollIdList);
		ClearScrollResponse response = clearScrollRequestBuilder.get();
		return response.isSucceeded();
	}

	/**
	 * 处理返回结果
	 *
	 * @param response
	 *            搜索的结果
	 * @return 结果
	 */
	private EsResult getResult(SearchResponse response, EsResult esResult) {

		SearchHits searchHits = response.getHits();

		List<Map<String, Object>> resultList = null;

		log.debug("共匹配到:" + searchHits.getTotalHits() + "条记录!");

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

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch().setQuery(queryBuilder);

		if (esQuery != null) {
			// 索引与文档类型
			if (!StringUtils.isEmpty(esQuery.getIndex())) {
				searchRequestBuilder.setIndices(esQuery.getIndex());
				if (!StringUtils.isEmpty(esQuery.getType())) {
					searchRequestBuilder.setTypes(esQuery.getType());
				}
			}
			// 搜索方式
			if (esQuery.getSearchType() == null) {
				searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			} else {
				searchRequestBuilder.setSearchType(esQuery.getSearchType());
			}
			// 分页
			if (esQuery.getPageNo() != null && esQuery.getPageSize() != null) {
				int from = (esQuery.getPageNo() - 1) * esQuery.getPageSize();
				esResult.setPageNo(from);
				esResult.setPageSize(esQuery.getPageSize());
				searchRequestBuilder.setFrom(from).setSize(esQuery.getPageSize());

			} else {
				// 默认还是要分页，查前50条
				esResult.setPageNo(0);
				esResult.setPageSize(50);
				searchRequestBuilder.setFrom(0).setSize(50);
			}
			// 排序
			if (esQuery.getSortFiled() != null && esQuery.getSort() != null) {
				searchRequestBuilder.addSort(esQuery.getSortFiled(), esQuery.getSort());
			}
			searchRequestBuilder.setExplain(esQuery.isExplain());
		}

		return getResult(searchRequestBuilder.execute().actionGet(), esResult);
	}

	/**
	 * 没有实现全部操作的封装，可以获取client自定义开发
	 *
	 * @return client
	 */
	public Client getClient() {

		return client;
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

		IndicesExistsRequestBuilder builder = client.admin().indices().prepareExists(index);
		IndicesExistsResponse res = builder.get();

		return res.isExists();
	}
}
