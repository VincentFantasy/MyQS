package com.example.elasticsearch.pojo;

import lombok.Data;

@Data
public class TwoPointDistanceQueryVo {

	private GeoPoint one;

	private GeoPoint two;
}
