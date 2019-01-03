package com.example.elasticsearch.pojo.es;

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
     */
    private String index;

    /**
     * 文档类型名
     */
    private String type;

    /**
     * 文档的id，用于更新
     */
    private String id;

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

    public EsQuery() {
    }

    public EsQuery(String index, String type, String sortFiled, SortOrder sort) {
        this.index = index;
        this.type = type;
        this.sortFiled = sortFiled;
        this.sort = sort;
    }

    public EsQuery(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public EsQuery(String index, String type, String id, String sortFiled, SortOrder sort, Long scrollTimeout) {
        this.index = index;
        this.type = type;
        this.id = id;
        this.sortFiled = sortFiled;
        this.sort = sort;
        this.scrollTimeout = scrollTimeout;
    }

    public EsQuery(String index, String type, String id, String sortFiled, SortOrder sort, Long scrollTimeout, Integer pageNo, Integer pageSize) {
        this.index = index;
        this.type = type;
        this.id = id;
        this.sortFiled = sortFiled;
        this.sort = sort;
        this.scrollTimeout = scrollTimeout;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "EsQuery{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", sortFiled='" + sortFiled + '\'' +
                ", sort=" + sort +
                ", scrollTimeout=" + scrollTimeout +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getScrollTimeout() {
        return scrollTimeout;
    }

    public void setScrollTimeout(Long scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortFiled() {
        return sortFiled;
    }

    public void setSortFiled(String sortFiled) {
        this.sortFiled = sortFiled;
    }

    public SortOrder getSort() {
        return sort;
    }

    public void setSort(SortOrder sort) {
        this.sort = sort;
    }
}
