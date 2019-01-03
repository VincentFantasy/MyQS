package com.example.elasticsearch.pojo.es;

import com.example.elasticsearch.constant.EsFiledsIndexConstant;
import com.example.elasticsearch.constant.EsFiledsTypeConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档属性
 *
 * @author tanzhi
 *
 */
public final class EsField {
	/**
	 * 字段名
	 */
	private String name;
	/**
	 * 字段的数据类型
	 */
	private EsFiledsTypeConstant type;
	/**
	 * 该属性控制字段是否编入索引被搜索，该属性共有三个有效值：analyzed、no和not_analyzed
	 */
	private EsFiledsIndexConstant index;
	/**
	 * 用于建立索引和搜索的分析器名称
	 */
	private String analyzer;
	/**
	 * 分析器
	 */
	private String searchAnalyzer;
	/**
	 * 日期格式
	 */
	//private String format;
	/**
	 * 还有很多其他很少用到字段属性，可以加入other
 	 */
	private Map<String, String> other = new HashMap<>(20);

	public EsField() {
	}

	public EsField(String name, EsFiledsTypeConstant type, EsFiledsIndexConstant index, String analyzer, String searchAnalyzer) {
		this.name = name;
		this.type = type;
		this.index = index;
		this.analyzer = analyzer;
		this.searchAnalyzer = searchAnalyzer;
	}

	public EsField(String name, EsFiledsTypeConstant type, EsFiledsIndexConstant index, String analyzer, String searchAnalyzer, Map<String, String> other) {
		this.name = name;
		this.type = type;
		this.index = index;
		this.analyzer = analyzer;
		this.searchAnalyzer = searchAnalyzer;
		this.other = other;
	}

	public void setOther(String key, String value) {
		this.other.put(key, value);
	}

	public String getOther(String key) {
		return this.other.get(key);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EsFiledsTypeConstant getType() {
		return type;
	}

	public void setType(EsFiledsTypeConstant type) {
		this.type = type;
	}

	public EsFiledsIndexConstant getIndex() {
		return index;
	}

	public void setIndex(EsFiledsIndexConstant index) {
		this.index = index;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getSearchAnalyzer() {
		return searchAnalyzer;
	}

	public void setSearchAnalyzer(String searchAnalyzer) {
		this.searchAnalyzer = searchAnalyzer;
	}

	public Map<String, String> getOther() {
		return other;
	}

	public void setOther(Map<String, String> other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "EsField{" +
				"name='" + name + '\'' +
				", type=" + type +
				", index=" + index +
				", analyzer='" + analyzer + '\'' +
				", searchAnalyzer='" + searchAnalyzer + '\'' +
				", other=" + other +
				'}';
	}
}
