package com.example.elasticsearch;

import com.example.myes.constant.EsFiledsIndexConstant;
import com.example.myes.constant.EsFiledsTypeConstant;
import com.example.myes.pojo.es.EsData;
import com.example.myes.pojo.es.EsField;
import com.example.myes.pojo.es.EsQuery;
import com.example.myes.pojo.es.EsType;
import com.example.myes.repository.ElasticsearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTest {

	@Autowired
	private ElasticsearchRepository elastcisearch;

	private String index;

	private String type;

	@Before
	public void before() {

		index = "test";

		type = "test2";
	}

	@Test
	public void test() {

	}

	@Test
	public void addIndex() {

		System.out.println(elastcisearch.createIndex(index));
	}

	@Test
	public void deleteIndex() {

		System.out.println(elastcisearch.deleteIndex(index));
	}

	@Test
	public void hasIndex() {

		System.out.println(elastcisearch.hasIndex(index));
	}

	@Test
	public void type() {

		EsField esField = new EsField("name", EsFiledsTypeConstant.STRING,
				EsFiledsIndexConstant.ANALYZED, "ik_max_word", "ik_max_word");
		List<EsField> list = new ArrayList<>(1);
		list.add(esField);

		EsType esType = new EsType(index, type, list);

		try {
			System.out.println(elastcisearch.createType(esType));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
    public void insertData() {
	    Map<String, String> map = new HashMap<>(1);
	    map.put("name", "这是一段测试的文字5");

        EsData data = new EsData(index, type, map);

        System.out.println(elastcisearch.insertData(data));
    }

    @Test
    public void updateData() throws ExecutionException, InterruptedException {
        Map<String, String> map = new HashMap<>(1);
        map.put("name", "张三");

        String id = "AWgRtNQRjdnS1tIfKlj1";

        EsData data = new EsData(index, type, id, map);

        System.out.println(elastcisearch.updateData(data));
    }

    @Test
    public void deleteData() {
        String id = "AWgRtNQRjdnS1tIfKlj1";

        EsData data = new EsData(index, type, id);

        System.out.println(elastcisearch.deleteData(data));
    }

    @Test
    public void matchQuery() {
        System.out.println(elastcisearch.matchAllQuery(null, new EsQuery().setPageNo(1).setPageSize(10)));
    }

    @Test
    public void matchQueryField() {
        System.out.println(elastcisearch.matchQuery("name", "国歌"));
    }

}
