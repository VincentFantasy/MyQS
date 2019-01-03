package com.example.elasticsearch.vo;

import lombok.Data;

@Data
public class EsField {

	private String fieldName;

	private String type;

	private String index;

	private String analyzer;

	private String searchAnalyzer;

	private String format;

}
