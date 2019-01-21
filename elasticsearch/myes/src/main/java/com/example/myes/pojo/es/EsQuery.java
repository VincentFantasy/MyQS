package com.example.myes.pojo.es;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.sort.SortOrder;

/**
 * 通用搜索实体
 *
 * @author tanzhi
 *
 */
public class EsQuery {

    /**
     * 索引名
     * 可以修改为多索引搜索
     */
    private String index;

    /**
     * 文档类型名
     * 可以修改为多类型搜索
     */
    private String type;

    /**
     * 文档的id，用于更新
     */
    private String id;

    /**
     * 搜索的字段
     */
    private String field;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 排序的字段
     */
    private String sortFiled;

    /**
     * 分页的方式
     */
    private SortOrder sort;

    /**
     * 滚动搜索请求指定超时时长，单位毫秒
     */
    private Long scrollTimeout;

    /**
     * 页号
     */
    private Integer pageNo;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 搜索类型，可以为null，默认；一般这里填null就可以，其他搜索方式可以看SearchType枚举的源码
     */
    private SearchType searchType;

    /**
     * 设置是否按查询匹配度排序
     */
    private boolean explain = false;

    public EsQuery() {
    }

    public String getKeyword() {
        return keyword;
    }

    public EsQuery setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    @Override
    public String toString() {
        return "EsQuery{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", field='" + field + '\'' +
                ", keyword='" + keyword + '\'' +
                ", sortFiled='" + sortFiled + '\'' +
                ", sort=" + sort +
                ", scrollTimeout=" + scrollTimeout +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", searchType=" + searchType +
                ", explain=" + explain +
                '}';
    }

    public String getField() {
        return field;
    }

    public EsQuery setField(String field) {
        this.field = field;
        return this;
    }

    public boolean isExplain() {
        return explain;
    }

    public EsQuery setExplain(boolean explain) {
        this.explain = explain;
        return this;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public EsQuery setSearchType(SearchType searchType) {
        this.searchType = searchType;
        return this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public EsQuery setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public EsQuery setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Long getScrollTimeout() {
        return scrollTimeout;
    }

    public EsQuery setScrollTimeout(Long scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public EsQuery setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public EsQuery setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public EsQuery setId(String id) {
        this.id = id;
        return this;
    }

    public String getSortFiled() {
        return sortFiled;
    }

    public EsQuery setSortFiled(String sortFiled) {
        this.sortFiled = sortFiled;
        return this;
    }

    public SortOrder getSort() {
        return sort;
    }

    public EsQuery setSort(SortOrder sort) {
        this.sort = sort;
        return this;
    }
}
