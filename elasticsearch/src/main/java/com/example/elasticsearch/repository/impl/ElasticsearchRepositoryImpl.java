package com.example.elasticsearch.repository.impl;

import com.example.elasticsearch.pojo.es.EsData;
import com.example.elasticsearch.pojo.es.EsField;
import com.example.elasticsearch.pojo.es.EsQuery;
import com.example.elasticsearch.pojo.es.EsType;
import com.example.elasticsearch.repository.ElasticsearchRepository;
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
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

		if (StringUtils.isEmpty(index))
			throw new RuntimeException("索引名不能为空");

		return thisHasIndex(index);
	}

	@Override
	public boolean createIndex(String index) {

		/*
		 * if (StringUtils.isEmpty(index)) throw new RuntimeException("索引名不能为空");
		 */

		if (thisHasIndex(index))
			throw new RuntimeException("该索引已存在:" + index);

		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
				.prepareCreate(index);
		CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
		// log.info(createIndexResponse.getContext().toString());
		// log.info(createIndexResponse.getHeaders().toString());

		return response.isAcknowledged();
	}

	@Override
	public boolean deleteIndex(String index) {

		if (!thisHasIndex(index))
			throw new RuntimeException("该索引不存在:" + index);

		DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices()
				.prepareDelete(index);
		DeleteIndexResponse response = deleteIndexRequestBuilder.get();

		return response.isAcknowledged();
	}

	@Override
	public boolean createType(EsType type) throws IOException {

		if (type == null)
			throw new RuntimeException("type不能为空");

		if (!thisHasIndex(type.getIndex()))
			throw new RuntimeException("该索引不存在:" + type.getIndex());

		if (StringUtils.isEmpty(type.getType()))
			throw new RuntimeException("文档类型不能为空");

		if (type.getFields().size() == 0)
			throw new RuntimeException("需要为文档类型添加字段");

		PutMappingRequest mapping = Requests.putMappingRequest(type.getIndex()).type(type.getType())
				.source(getMapping(type.getFields()));
		PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping)
				.actionGet();

		return putMappingResponse.isAcknowledged();
	}

	@Override
	public boolean insertData(EsData data) {

		if (data == null)
			throw new RuntimeException("data不能为空");

		if (!thisHasIndex(data.getIndex()))
			throw new RuntimeException("该索引不存在:" + data.getIndex());

		if (StringUtils.isEmpty(data.getType()))
			throw new RuntimeException("文档类型不能为空");

		IndexResponse response = client.prepareIndex(data.getIndex(), data.getType())
				.setSource(data.getData()).get();
		log.debug("version:" + response.getVersion());

		return response.isCreated();
	}

	@Override
	public boolean updateData(EsData data) throws ExecutionException, InterruptedException {

		if (data == null)
			throw new RuntimeException("data不能为空");

		if (!thisHasIndex(data.getIndex()))
			throw new RuntimeException("该索引不存在:" + data.getIndex());

		if (StringUtils.isEmpty(data.getType()))
			throw new RuntimeException("文档类型不能为空");

		if (StringUtils.isEmpty(data.getId()))
			throw new RuntimeException("文档id不能为空");

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

		if (data == null)
			throw new RuntimeException("data不能为空");

		if (!thisHasIndex(data.getIndex()))
			throw new RuntimeException("该索引不存在:" + data.getIndex());

		if (StringUtils.isEmpty(data.getType()))
			throw new RuntimeException("文档类型不能为空");

		if (StringUtils.isEmpty(data.getId()))
			throw new RuntimeException("文档id不能为空");

		DeleteResponse response = client
				.prepareDelete(data.getIndex(), data.getType(), data.getId()).get();
		log.debug("version:" + response.getVersion());

		return response.isFound();
	}

	@Override
	public List<Map<String, Object>> matchAllQuery(SearchType searchType, EsQuery esQuery) {

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setQuery(QueryBuilders.matchAllQuery());
		// 搜索方式
		if (searchType == null) {
			searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		} else {
			searchRequestBuilder.setSearchType(searchType);
		}
		// 分页
		if (esQuery != null && esQuery.getPageNo() != null && esQuery.getPageSize() != null) {
			searchRequestBuilder.setFrom((esQuery.getPageNo() - 1) * esQuery.getPageSize())
					.setSize(esQuery.getPageSize());
		} else {
			searchRequestBuilder.setFrom(0)
					.setSize(60);
		}
		// 排序
		if (esQuery != null && esQuery.getSortFiled() != null && esQuery.getSort() != null) {
			searchRequestBuilder.addSort(esQuery.getSortFiled(), esQuery.getSort());
		}
		// 设置是否按查询匹配度排序
		SearchResponse response =  searchRequestBuilder.setExplain(true).execute().actionGet();

		return getResult(response.getHits());
	}

	@Override
	public List<Map<String, Object>> matchQuery(String field, String keyword) {

		if (StringUtils.isEmpty(field))
			throw new RuntimeException("搜索字段不能为空");

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		QueryBuilder queryBuilder = QueryBuilders.matchQuery(field, keyword);
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> matchMultiQuery(String[] field, String keyword) {

		if (field == null || field.length == 0)
			throw new RuntimeException("搜索字段不能为空");

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, keyword);
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> boolQuery(BoolQueryBuilder boolQueryBuilder, int pageno,
			int pagesize, SortBuilder sortBuilder) {

		return searchFunction(boolQueryBuilder, pageno, pagesize, sortBuilder);
	}

	@Override
	public List<Map<String, Object>> queryString(String keyword) {

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		// 通配符查询
		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("*" + keyword + "*");
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> termQuery(String field, String keyword) {

		if (StringUtils.isEmpty(field))
			throw new RuntimeException("搜索字段不能为空");

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		QueryBuilder queryBuilder = QueryBuilders.termQuery(field, keyword);
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> fuzzyQuery(String field, String keyword) {

		if (StringUtils.isEmpty(field))
			throw new RuntimeException("搜索字段不能为空");

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery(field, keyword);
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> wildcardQuery(String field, String keyword) {

		if (StringUtils.isEmpty(field))
			throw new RuntimeException("搜索字段不能为空");

		if (StringUtils.isEmpty(keyword))
			throw new RuntimeException("搜索词不能为空");

		QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(field, keyword);
		return searchFunction(queryBuilder);
	}

	@Override
	public List<Map<String, Object>> searchScrollFunction(EsQuery esQuery) {

		if (esQuery == null)
			throw new RuntimeException("data不能为空");

		if (!thisHasIndex(esQuery.getIndex()))
			throw new RuntimeException("该索引不存在:" + esQuery.getIndex());

		if (StringUtils.isEmpty(esQuery.getType()))
			throw new RuntimeException("文档类型不能为空");

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
				.setIndices(esQuery.getIndex())
				.setTypes(esQuery.getType())
				.setQuery(QueryBuilders.matchAllQuery());
		// 分页
		if (esQuery.getPageNo() != null && esQuery.getPageSize() != null) {
			searchRequestBuilder.setFrom((esQuery.getPageNo() - 1) * esQuery.getPageSize())
					.setSize(esQuery.getPageSize());
		} else {
			searchRequestBuilder.setFrom(0)
					.setSize(10);
		}
		// 排序
		if (esQuery.getSortFiled() != null && esQuery.getSort() != null) {
			searchRequestBuilder.addSort(esQuery.getSortFiled(), esQuery.getSort());
		}
		// 时长
		if (esQuery.getScrollTimeout() != null) {
			searchRequestBuilder.setScroll(new TimeValue(esQuery.getScrollTimeout()));
		} else {
			searchRequestBuilder.setScroll(new TimeValue(60000));
		}

		SearchResponse scrollResp = searchRequestBuilder.execute().actionGet();
		// 获取总数量
		/*long totalCount = scrollResp.getHits().getTotalHits();
		// 计算总页数,每次搜索数量为分片数*设置的size大小
		// int page = (int) totalCount / (5 * 10);

		int page = (int) totalCount / 10;

		System.out.println("总记录数：" + totalCount);
		System.out.println("总页数：" + page);

		String scrollId = scrollResp.getScrollId();
		System.out.println("scrollId: " + scrollId);*/

		return getResult(scrollResp.getHits());
	}

	@Override
	public List<Map<String, Object>> searchByScrollId(String scrollId) {
		// 再次发送请求,并使用上次搜索结果的ScrollId
		SearchResponse scrollResp = client.prepareSearchScroll(scrollId)
				.setScroll(new TimeValue(60000)).execute().actionGet();

		return getResult(scrollResp.getHits());
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
	 * @param searchHits
	 *            搜索的结果
	 * @return 结果
	 */
	private List<Map<String, Object>> getResult(SearchHits searchHits) {

		List<Map<String, Object>> resultList = null;

		log.debug("共匹配到:" + searchHits.getTotalHits() + "条记录!");

		if (searchHits.getTotalHits() > 0) {
			resultList = new ArrayList<>((int) searchHits.getTotalHits());
			for (SearchHit searchHit : searchHits) {
				resultList.add(searchHit.getSource());
			}
		}

		return resultList;
	}

	/**
	 * 搜索查询
	 *
	 * @param queryBuilder
	 *            查询条件
	 * @return 结果
	 */
	private List<Map<String, Object>> searchFunction(QueryBuilder queryBuilder) {

		SearchResponse response = client.prepareSearch().setQuery(queryBuilder).setFrom(0)
				.setSize(100).execute().actionGet();

		return getResult(response.getHits());
	}

	/**
	 * 根据from-size进行分页查询（默认分页深度的10000，如果超过10000就会报错）
	 *
	 * @param boolQueryBuilder
	 *            布尔查询
	 * @param pageno
	 *            页号
	 * @param pagesize
	 *            页大小
	 * @return 结果
	 */
	private List<Map<String, Object>> searchFunction(BoolQueryBuilder boolQueryBuilder, int pageno,
			int pagesize, SortBuilder sortBuilder) {

		int from = (pageno - 1) * pagesize;

		SearchResponse response;
		if (sortBuilder != null) {
			response = client.prepareSearch().setQuery(boolQueryBuilder).setFrom(from)
					.setSize(pagesize).addSort(sortBuilder).execute().actionGet();
		} else {
			response = client.prepareSearch().setQuery(boolQueryBuilder).setFrom(from)
					.setSize(pagesize).execute().actionGet();
		}

		return getResult(response.getHits());
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
	 * source可以使用Object..或Map参数，但都是后面都是转换成XContentBuilder再使用
	 *
	 * @param fieldList
	 *            字段及字段属性
	 * @return 构成的XContentBuilder
	 */
	private XContentBuilder getMapping(List<EsField> fieldList) throws IOException {

		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
				.startObject("properties");

		for (EsField field : fieldList) {
			mapping.startObject(field.getName());

			if (field.getType() != null) {
				mapping.field("type", field.getType().getValue());
			}
			if (field.getIndex() != null) {
				mapping.field("index", field.getIndex().getValue());
			}
			if (StringUtils.isEmpty(field.getAnalyzer())) {
				mapping.field("analyzer", field.getAnalyzer());
			}
			if (StringUtils.isEmpty(field.getSearchAnalyzer())) {
				mapping.field("searchAnalyzer", field.getSearchAnalyzer());
			}
			for (Map.Entry<String, String> entry : field.getOther().entrySet()) {
				if (StringUtils.isEmpty(entry.getValue()))
					mapping.field(entry.getKey(), entry.getValue());
			}

			mapping.endObject();
		}
		mapping.endObject().endObject();

		return mapping;
	}

	/**
	 * 检验索引是否存在
	 *
	 * @param index
	 *            索引
	 * @return 是否存在
	 */
	private boolean thisHasIndex(String index) {

		if (StringUtils.isEmpty(index))
			throw new RuntimeException("索引名不能为空");

		IndicesExistsRequestBuilder builder = client.admin().indices().prepareExists(index);
		IndicesExistsResponse res = builder.get();

		return res.isExists();
	}
}
