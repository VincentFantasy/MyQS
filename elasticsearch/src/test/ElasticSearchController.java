package com.example.elasticsearch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.example.elasticsearch.entity.MallApp;
import com.example.elasticsearch.pojo.GoodsIndexQueryVo;
import com.example.elasticsearch.pojo.GoodsLocationQueryVo;
import com.example.elasticsearch.pojo.PolygonQueryVo;
import com.example.elasticsearch.pojo.TwoPointDistanceQueryVo;
import com.example.elasticsearch.service.GoodsService;
import com.example.elasticsearch.util.PageInfo;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author wangpeng1
 * @since 2018年10月23日
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private GoodsService goodsService;

	/**
	 * 获取IK分词结果
	 * 
	 * @param searchContent
	 * @return
	 */
	@GetMapping("/getIkTerms")
	public List<String> add(@RequestParam(name = "searchContent") String searchContent) {

		return getIkAnalyzeSearchTerms(searchContent);
	}

	private List<String> getIkAnalyzeSearchTerms(String searchContent) {

		// 调用IK分词器进行分词
		AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(
				elasticsearchTemplate.getClient(), AnalyzeAction.INSTANCE, "demo", searchContent);
		ikRequest.setTokenizer("ik_max_word");
		List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet()
				.getTokens();

		// 循环赋值
		List<String> searchTermList = new ArrayList<String>();
		ikTokenList.forEach(ikToken -> {
			searchTermList.add(ikToken.getTerm());
		});
		return searchTermList;
	}

	@GetMapping("/createIndex")
	public String createIndex(@RequestParam String indexName) {

		return ElasticSearchUtil.createIndex(indexName);
	}

	@PostMapping("/createType")
	public String createType(@RequestBody @Valid EsIndexField index) {

		return ElasticSearchUtil.createType(index.getIndex(), index.getType(),
				index.getFieldList());
	}

	@GetMapping("/deleteIndex")
	public String deleteIndex(@RequestParam String indexName) {

		return ElasticSearchUtil.deleteIndex(indexName);
	}

	@PostMapping("/insertData")
	public String insertData(@RequestBody @Valid EsIndex index) {

		return ElasticSearchUtil.insertData(index.getIndex(), index.getType(), index.getContent());
	}

	@PostMapping("/updateData")
	public String updateData(@RequestBody @Valid EsIndex index) {

		return ElasticSearchUtil.updateData(index.getIndex(), index.getType(), index.getId(),
				index.getContent());
	}

	@PostMapping("/deleteData")
	public String deleteData(@RequestBody @Valid EsIndex index) {

		return ElasticSearchUtil.deleteData(index.getIndex(), index.getType(), index.getId());
	}

	@PostMapping("/matchAllQuery")
	public List<Map<String, Object>> matchAllQuery() {

		return ElasticSearchUtil.matchAllQuery();
	}

	@PostMapping("/matchQuery")
	public List<Map<String, Object>> matchQuery(@RequestBody @Valid EsSearchField searchField) {

		return ElasticSearchUtil.matchQuery(searchField.getFieldName(), searchField.getKeyword());
	}

	@PostMapping("/matchMultiQuery")
	public List<Map<String, Object>> matchMultiQuery(
			@RequestBody @Valid EsSearchFields searchFields) {

		return ElasticSearchUtil.matchMultiQuery(searchFields.getFieldNames(),
				searchFields.getKeyword());
	}

	@PostMapping("/queryString")
	public List<Map<String, Object>> queryString(@RequestBody @Valid EsSearchField searchField) {

		return ElasticSearchUtil.queryString(searchField.getKeyword());
	}

	@PostMapping("/termQuery")
	public List<Map<String, Object>> termQuery(@RequestBody @Valid EsSearchField searchField) {

		return ElasticSearchUtil.termQuery(searchField.getFieldName(), searchField.getKeyword());

	}

	@PostMapping("/fuzzyQuery")
	public List<Map<String, Object>> fuzzyQuery(@RequestBody @Valid EsSearchField searchField) {

		return ElasticSearchUtil.fuzzyQuery(searchField.getFieldName(), searchField.getKeyword());

	}

	@PostMapping("/wildcardQuery")
	public List<Map<String, Object>> wildcardQuery(@RequestBody @Valid EsSearchField searchField) {

		return ElasticSearchUtil.wildcardQuery(searchField.getFieldName(),
				searchField.getKeyword());
	}

	@PostMapping("/searchScrollFunction")
	public List<Map<String, Object>> searchScrollFunction() throws Exception {

		return ElasticSearchUtil.searchScrollFunction();
	}

	@GetMapping("/searchByScrollId")
	public List<Map<String, Object>> searchByScrollId(@RequestParam String scrollId)
			throws Exception {

		return ElasticSearchUtil.searchByScrollId(scrollId);
	}

	@GetMapping("/importGoodsData")
	public List<Map<String, Object>> importGoodsData(@RequestParam String index,
			@RequestParam String type) throws Exception {

		return goodsService.importGoodsData(index, type);
	}

	@PostMapping("/queryIndex")
	public PageInfo queryIndex(@RequestBody @Valid GoodsIndexQueryVo goodsIndexQueryVo)
			throws Exception {

		return goodsService.queryIndex(goodsIndexQueryVo);
	}

	@PostMapping("/queryByLocation")
	public PageInfo queryByLocation(@RequestBody @Valid GoodsLocationQueryVo goodsLocationQueryVo)
			throws Exception {

		return goodsService.queryPageByLocation(goodsLocationQueryVo);
	}

	@PostMapping("/queryListByLocation")
	public List<Map<String, Object>> queryListByLocation(
			@RequestBody @Valid GoodsLocationQueryVo goodsLocationQueryVo) throws Exception {

		return goodsService.queryListByLocation(goodsLocationQueryVo);
	}

	@PostMapping("/queryListByPolygon")
	public List<Map<String, Object>> queryListByPolygon(
			@RequestBody @Valid PolygonQueryVo polygonQueryVo) throws Exception {

		return goodsService.queryListByPolygon(polygonQueryVo);
	}

	@PostMapping("/getPointToPoint")
	public double getPointToPoint(
			@RequestBody @Valid TwoPointDistanceQueryVo twoPointDistanceQueryVo) throws Exception {

		return goodsService.getPointToPoint(twoPointDistanceQueryVo);
	}

	@PostMapping("/findAllMallApp")
	public List<MallApp> findAllMallApp() throws Exception {

		return mallAppService.findAll();
	}
}
