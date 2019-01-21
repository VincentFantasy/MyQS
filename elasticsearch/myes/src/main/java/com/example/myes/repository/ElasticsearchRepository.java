package com.example.myes.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.myes.pojo.es.EsResult;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import com.example.myes.pojo.es.EsData;
import com.example.myes.pojo.es.EsQuery;
import com.example.myes.pojo.es.EsType;

/**
 * Elasticsearch操作
 *
 * @author tanzhi
 * @version 1.0
 */
public interface ElasticsearchRepository {

	/**
	 * 索引是否存在
	 *
	 * @param index
	 *            索引名称
	 * @return 该索引是否存在
	 */
	boolean hasIndex(String index);

	/**
	 * 创建索引
	 *
	 * @param index
	 *            索引名称
	 * @return 是否创建成功
	 */
	boolean createIndex(String index);

	/**
	 * 刪除索引
	 *
	 * @param index
	 *            索引名称
	 * @return 是否删除成功
	 */
	boolean deleteIndex(String index);

	/**
	 * 创建索引文档类型
	 *
	 * @param type
	 *            文档类型
	 * @return 是否创建成功
	 */
	boolean createType(EsType type) throws IOException;

	/**
	 * 插入数据
	 *
	 * @param data
	 *            数据
	 * @return 文档id
	 */
	String insertData(EsData data);

	/**
	 * 更新数据信息
	 *
	 * @param data
	 *            数据, 必须传入id
	 *
	 * @return 是新建,否创建。。
	 */
	boolean updateData(EsData data) throws ExecutionException, InterruptedException;

	/**
	 * 删除数据
	 *
	 * @param data
	 *            数据, 必须传入id，不用传入map.data
	 * @return 是否删除成功，否一般是数据不存在
	 */
	boolean deleteData(EsData data);

	/**
	 * 查询全部
	 *
	 * @param esQuery
	 *            这里可以设置分页，和排序
	 * @return 结果
	 */
	EsResult matchAllQuery(EsQuery esQuery);

	/**
	 * 单个字段IK查找 (matchQuery会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到)
	 *
	 * @param field
	 *            搜索的字段名
	 * @param keyword
	 *            搜索的关键字
	 * @return 搜索结果
	 */
	EsResult matchQuery(String field, String keyword);

	/**
	 * 多个字段IK查找 (matchQuery会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到)
	 *
	 * @param field
	 *            多个字段
	 * @param keyword
	 *            关键字
	 * @return 搜索结果
	 */
	EsResult matchMultiQuery(String[] field, String keyword);

	/**
	 * 匹配其他查询的布尔组合的文档的查询。
	 *
	 * @param boolQueryBuilder
	 *            布尔查询
	 * @param esQuery
	 *            可以分页，排序等
	 * @return 搜索结果
	 */
	EsResult boolQuery(BoolQueryBuilder boolQueryBuilder, EsQuery esQuery);

	/**
	 * 单个字符串匹配查询
	 *
	 * @param esQuery
	 *            需要填入keyword关键字，会在两边加入*进行匹配查询 可以分页，排序等
	 * @return 搜索结果
	 */
	EsResult queryString(EsQuery esQuery);

	/**
	 * 单个字段精确查找 (不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到)
	 *
	 * @param esQuery
	 *            需要填入field搜索的字段, keyword关键字，会在两边加入*进行匹配查询 可以分页，排序等
	 * @return 搜索结果
	 */
	EsResult termQuery(EsQuery esQuery);

	/**
	 * 单个字段模糊查询
	 *
	 * @param esQuery
	 *            需要填入field搜索的字段, keyword关键字，会在两边加入*进行匹配查询 可以分页，排序等
	 * @return 搜索结果
	 */
	EsResult fuzzyQuery(EsQuery esQuery);

	/**
	 * 通配符查询
	 *
	 * @param esQuery
	 *            需要填入field搜索的字段, keyword关键字，会在两边加入*进行匹配查询 可以分页，排序等
	 * @return 搜索结果
	 */
	EsResult wildcardQuery(EsQuery esQuery);

	/**
	 * scroll分页
	 *
	 * @return 搜索结果
	 */
	EsResult searchScrollFunction(EsQuery query);

	/**
	 * 根据scrollId查询，每次调用只要scrollId在有效期内，则都会自动查询下一页的数据
	 *
	 * @param scrollId
	 *            scrollId
	 * @return 搜索结果
	 */
	EsResult searchByScrollId(String scrollId);

	/**
	 * 清除滚动ID
	 *
	 * @param scrollId
	 *            滚动ID
	 * @return 是否清除成功
	 */
	boolean clearScroll(String scrollId);

	/**
	 * 批量清除滚动ID
	 *
	 * @param scrollIdList
	 *            滚动ID列表
	 * @return 是否清除成功
	 */
	boolean clearScroll(List<String> scrollIdList);
}
