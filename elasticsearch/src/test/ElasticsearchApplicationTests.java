package com.example.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.example.elasticsearch.pojo.es.EsField;
import ElasticSearchUtil;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

	@Autowired
	private Client client;

	@Test
	public void test() {
		String indexName = "test";

		System.out.println(isIndexExists(indexName));
	}

	@Test
	public void contextLoads() {
		System.out.println(createIndex("test"));
	}

	/**
	 * 判断是否存在该索引
	 *
	 * @param indexName
	 *            索引名称
	 * @return
	 */
	public boolean isIndexExists(String indexName) {

		IndicesExistsRequestBuilder builder = client.admin().indices().prepareExists(indexName);
		IndicesExistsResponse res = builder.get();
		return res.isExists();
	}

	/**
	 * 创建索引
	 *
	 * @param indexName
	 */
	public String createIndex(String indexName) {

		if (isIndexExists(indexName)) {
			throw new RuntimeException("索引对象已经存在，无法创建！");
		}

		client.admin().indices().prepareCreate(indexName).execute().actionGet();

		return "创建索引成功";
	}

	/**
	 * 创建索引
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            索引type
	 * @param sourcecontent
	 *            要索引的内容
	 */
	public void createIndex(String index, String type, String sourcecontent) {

		if (isIndexExists(index)) {
			throw new RuntimeException("索引对象已经存在，无法创建！");
		}

		IndexResponse response = client.prepareIndex(index, type).setSource(sourcecontent).execute()
				.actionGet();
		/*printIndexInfo(response);*/
	}

	@Autowired
	private ElasticSearchUtil elasticSearchUtil;

	@Test
	public void name() {
		List<EsField> esFields = new ArrayList<>(1);

		EsField esField = new EsField();
		esField.setIndex("analyzed");
		esField.setFieldName("name");
		//esField.setFormat("utf-8");
		esField.setType("string");
		esField.setAnalyzer("ik_max_word");
		esField.setSearchAnalyzer("ik_max_word");

		/*EsField esField1 = new EsField();
		esField1.setIndex("test");
		esField1.setFormat("utf-8");
		esField1.setType("string");
		esField1.setFieldName("ts_id");
		esField1.setAnalyzer("ik_max_word");
		esField1.setSearchAnalyzer("ik_max_word");*/

		/*EsField esField2 = new EsField();
		esField2.setIndex("test");
		esField2.setFormat("utf-8");
		esField2.setType("string");
		esField2.setFieldName("time");
		esField2.setAnalyzer("ik_max_word");
		esField2.setSearchAnalyzer("ik_max_word");*/

		//esFields.add(esField1);
		esFields.add(esField);
		//esFields.add(esField2);

		System.out.println(elasticSearchUtil.createType("test", "test2", esFields));
	}

	@Test
	public void data() {
		JSONObject object = new JSONObject(1);
		//object.put("name", "中华人民共和国国歌");
		object.put("name", "这是一段测试的文字");

		System.out.println(elasticSearchUtil.insertData("test", "test2", object));
	}

	@Test
	public void query() {
		System.out.println(elasticSearchUtil.matchQuery("name", "中华"));
	}

	@Test
	public void queryPage() {

	}

}
