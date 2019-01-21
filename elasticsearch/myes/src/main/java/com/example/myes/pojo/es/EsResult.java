package com.example.myes.pojo.es;

import java.util.List;
import java.util.Map;

/**
 * 搜索结果统一返回类
 *
 * @author tanzhi
 *
 */
public class EsResult {

    /**
     * 结果
     */
    List<Map<String, Object>> resluts;

    /**
     * 总数量
     */
    long count;

    /**
     * 页数
     */
    int pageNo;

    /**
     * 页大小
     */
    int pageSize;

    /**
     * 滚动id
     */
    String scrollId;

    public EsResult() {
    }

    public EsResult(List<Map<String, Object>> resluts, long count) {
        this.resluts = resluts;
        this.count = count;
    }

    public EsResult(List<Map<String, Object>> resluts, long count, int pageNo, int pageSize, String scrollId) {
        this.resluts = resluts;
        this.count = count;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.scrollId = scrollId;
    }

    public String getScrollId() {
        return scrollId;
    }

    public EsResult setScrollId(String scrollId) {
        this.scrollId = scrollId;
        return this;
    }

    @Override
    public String toString() {
        return "EsResult{" +
                "resluts=" + resluts +
                ", count=" + count +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", scrollId=" + scrollId +
                '}';
    }

    public List<Map<String, Object>> getResluts() {
        return resluts;
    }

    public EsResult setResluts(List<Map<String, Object>> resluts) {
        this.resluts = resluts;
        return this;
    }

    public long getCount() {
        return count;
    }

    public EsResult setCount(long count) {
        this.count = count;
        return this;
    }

    public int getPageNo() {
        return pageNo;
    }

    public EsResult setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public EsResult setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}
