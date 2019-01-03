package com.example.elasticsearch.repository;

import com.example.elasticsearch.pojo.es.EsData;
import com.example.elasticsearch.pojo.es.EsQuery;
import com.example.elasticsearch.pojo.es.EsType;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
	 * @return 是新建，否更新。。
	 */
	boolean insertData(EsData data);

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
     * @param searchType 搜索类型，可以为null，默认；一般这里填null就可以，其他搜索方式可以看SearchType枚举的源码
     * @param esQuery 这里可以设置分页，和排序
	 * @return 结果
	 */
	List<Map<String, Object>> matchAllQuery(SearchType searchType, EsQuery esQuery);

	/**
	 * 单个字段IK查找 (matchQuery会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到)
	 *
	 * @param field
	 *            搜索的字段名
	 * @param keyword
	 *            搜索的关键字
	 * @return 搜索结果
	 */
	List<Map<String, Object>> matchQuery(String field, String keyword);

	/**
	 * 多个字段IK查找 (matchQuery会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到)
	 *
	 * @param field
	 *            多个字段
	 * @param keyword
	 *            关键字
	 * @return 搜索结果
	 */
	List<Map<String, Object>> matchMultiQuery(String field[], String keyword);

	/**
	 * 匹配其他查询的布尔组合的文档的查询。
	 *
	 * @param boolQueryBuilder
	 *            布尔查询
	 * @param pageno
	 *            第几页
	 * @param pagesize
	 *            页大小
	 * @param sortBuilder
	 *            排序
	 * @return 搜索结果
	 */
	List<Map<String, Object>> boolQuery(BoolQueryBuilder boolQueryBuilder, int pageno, int pagesize,
			SortBuilder sortBuilder);

	/**
	 * 单个字符串匹配查询
	 *
	 * @param keyword
	 *            关键字，会在两边加入*进行匹配查询
	 * @return 搜索结果
	 */
	List<Map<String, Object>> queryString(String keyword);

	/**
	 * 单个字段精确查找 (不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到)
	 *
	 * @param field
	 *            字段
	 * @param keyword
	 *            关键字
	 * @return 搜索结果
	 */
	List<Map<String, Object>> termQuery(String field, String keyword);

	/**
	 * 单个字段模糊查询
	 *
	 * @param field
	 *            字段
	 * @param keyword
	 *            关键字
	 * @return 搜索结果
	 */
	List<Map<String, Object>> fuzzyQuery(String field, String keyword);

	/**
	 * 通配符查询
	 *
	 * @param field
	 *            字段
	 * @param keyword
	 *            关键字,没有经过处理，需要传入前先填好自定义通配符的字符串
	 * @return 搜索结果
	 */
	List<Map<String, Object>> wildcardQuery(String field, String keyword);

	/**
	 * scroll分页
	 *
	 * @return 搜索结果
	 */
	List<Map<String, Object>> searchScrollFunction(EsQuery query);

    /**
     * 根据scrollId查询，每次调用只要scrollId在有效期内，则都会自动查询下一页的数据
     *
     * @param scrollId scrollId
     * @return 搜索结果
     */
    List<Map<String, Object>> searchByScrollId(String scrollId);

    /**
     * 清除滚动ID
     *
     * @param scrollId 滚动ID
     * @return 是否清除成功
     */
    boolean clearScroll(String scrollId);

    /**
     * 批量清除滚动ID
     *
     * @param scrollIdList 滚动ID列表
     * @return 是否清除成功
     */
    boolean clearScroll(List<String> scrollIdList);
}
