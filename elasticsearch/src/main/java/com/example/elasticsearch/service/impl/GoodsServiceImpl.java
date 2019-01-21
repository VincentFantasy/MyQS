package com.example.elasticsearch.service.impl;

import com.example.elasticsearch.dao.DsGoodsRepository;
import com.example.elasticsearch.entity.DsGoods;
import com.example.elasticsearch.esdao.DemoGoodsRepository;
import com.example.elasticsearch.esdao.EsGoodsRepository;
import com.example.elasticsearch.esdao.EsStoreRepository;
import com.example.elasticsearch.esentity.DemoGoods;
import com.example.elasticsearch.esentity.ESStore;
import com.example.elasticsearch.esentity.EsGoods;
import com.example.elasticsearch.pojo.GoodsByRefIdQueryVo;
import com.example.elasticsearch.pojo.StoreIdQueryVo;
import com.example.elasticsearch.service.GoodsService;
import com.example.elasticsearch.util.JPQLQueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author tanzhi
 *
 */
@Service
@Slf4j
public class GoodsServiceImpl extends JPQLQueryUtil implements GoodsService {

	@Autowired
	private DsGoodsRepository goodsRepository;

	@Autowired
	private DemoGoodsRepository demoGoodsRepository;

	@Autowired
	private EsGoodsRepository esGoodsRepository;

	@Autowired
	private EsStoreRepository esStoreRepository;

	@Override
	public void importOne(String goodId) {

		DsGoods dsGoods = goodsRepository.findOne(goodId);
		if (dsGoods == null) {
			throw new RuntimeException("ds is null : " + goodId);
		}

		DemoGoods demoGoods = dsGoods2DemoGoods(dsGoods);

		demoGoodsRepository.save(demoGoods);
	}

	@Override
	public void importAll() {

		List<DsGoods> dsGoodsList = goodsRepository.findAll();
		if (dsGoodsList.size() == 0) {
			return;
		}

		List<DemoGoods> demoGoodsList = new ArrayList<>(dsGoodsList.size());
		dsGoodsList.forEach(dsGoods -> demoGoodsList.add(dsGoods2DemoGoods(dsGoods)));

		demoGoodsRepository.save(demoGoodsList);
	}

	@Override
	public DemoGoods findByGoodsId(String id) {
		if (id == null) {
			throw new RuntimeException("id不能为空");
		}

		return demoGoodsRepository.findByGoodsId(id);
	}

	@Override
	public Page<DemoGoods> searchByName(String name, int pageNo, int pageSize) {
		return demoGoodsRepository.findAllByGoodsName(name, new PageRequest(pageNo - 1, pageSize,
				new Sort(new Sort.Order(Sort.Direction.DESC, "goodsId"))));
	}

	@Override
	public Page<DemoGoods> queryLocationRange(double lat, double lon, int km) {

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withPageable(new PageRequest(0, 5, new Sort(new Sort.Order(Sort.Direction.DESC, "id"))));
		// 间接实现了QueryBuilder接口
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		// 以某点为中心，搜索指定范围
		// 定义查询单位：公里（多少公里以内）
		GeoDistanceQueryBuilder distanceQueryBuilder = new GeoDistanceQueryBuilder("location")
				.point(lat, lon)
				.distance(km, DistanceUnit.KILOMETERS);
		boolQueryBuilder.filter(distanceQueryBuilder);
		nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

		// 距离排序
		// 公里为单位
		// 正序，由小到大
		GeoDistanceSortBuilder distanceSortBuilder = new GeoDistanceSortBuilder("location")
				.point(lat, lon)
				.unit(DistanceUnit.KILOMETERS)
				.order(SortOrder.ASC);
		nativeSearchQueryBuilder.withSort(distanceSortBuilder);

		Page<DemoGoods> demoGoods = demoGoodsRepository.search(nativeSearchQueryBuilder.build());
		/*Page<DemoGoods> demoGoods = demoGoodsRepository.search(boolQueryBuilder, new PageRequest(0, 5));*/
		// 计算两点距离
		// GeoDistance.ARC和GeoDistance.PLANE，前者比后者计算起来要慢，但精确度要比后者高
		demoGoods.getContent().forEach(demoGoods1 -> {
			double distance = GeoDistance.ARC.calculate(lat, lon, demoGoods1.getLocation().getLat(),
					demoGoods1.getLocation().getLon(), DistanceUnit.KILOMETERS);
			System.out.println(demoGoods1.getGoodsName() + "距离:" + distance);
		});

		return demoGoods;
	}

	@Override
	public Page<EsGoods> queryGoodsByRef(GoodsByRefIdQueryVo goodsByRefIdQueryVo) {

		// 店铺上线 + refIds
		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		boolQueryBuilder1
				.must(QueryBuilders.termsQuery("goodsId", goodsByRefIdQueryVo.getRefIds()));
		boolQueryBuilder1.must(QueryBuilders.hasParentQuery("store",
				QueryBuilders.termQuery("storeState", 1)));
		List<EsGoods> esGoods = IteratorUtils
				.toList(esGoodsRepository.search(boolQueryBuilder1).iterator());
		if (esGoods == null || esGoods.size() == 0) {
			return null;
		}

		// 根据电子菜谱ID数组，分类ID，目标经纬度，搜索范围查询电子菜谱对应的点餐单
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 店铺状态
		boolQueryBuilder.must(QueryBuilders.hasParentQuery("store",
				QueryBuilders.termQuery("storeStare", 1)));
		// 商品状态
		boolQueryBuilder
				.must(QueryBuilders.termsQuery("goodsState", 1));

		// 距离 与 距离排序
		GeoDistanceSortBuilder geoDistanceSortBuilder = SortBuilders.geoDistanceSort("location")
				.point(goodsByRefIdQueryVo.getLatitude(), goodsByRefIdQueryVo.getLongitude())
				.unit(DistanceUnit.METERS).order(SortOrder.ASC);
		GeoDistanceQueryBuilder location = QueryBuilders.geoDistanceQuery("location")
				.point(goodsByRefIdQueryVo.getLatitude(), goodsByRefIdQueryVo.getLongitude())
				.distance(goodsByRefIdQueryVo.getDistance(), DistanceUnit.METERS);
		boolQueryBuilder.filter(QueryBuilders.hasParentQuery("store", location)
				.innerHit(new QueryInnerHitBuilder().addSort(geoDistanceSortBuilder)));
		nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
		// 获取， goodsByRefIdQueryRespVoMap用来转换成原实现所需，适配
		Page<EsGoods> page = esGoodsRepository.search(nativeSearchQueryBuilder.build());

		// 把数据包装，放进goodsByRefIdQueryRespVoMap
		if (page.hasContent()) {
			List<String> storeIds = new ArrayList<>(page.getContent().size());
			for (EsGoods eso : page.getContent()) {
				storeIds.add(eso.getGoodStoreId());
			}

			// 获取商品店铺信息 StoreCookRespVo
			List<ESStore> esStores = esStoreRepository.findAllByIdIn(storeIds);
			if (esStores == null || esStores.size() == 0) {
				throw new RuntimeException("商品所属店铺信息异常");
			}
			Map<String, ESStore> storeMap = new HashMap<>(esStores.size());
			for (ESStore esStore : esStores) {
				storeMap.put(esStore.getId(), esStore);
			}

			for (int i = 0; i < page.getContent().size(); i++) {
				EsGoods eso = page.getContent().get(i);

				// 计算距离
				double distance = GeoDistance.ARC.calculate(goodsByRefIdQueryVo.getLatitude(),
						goodsByRefIdQueryVo.getLongitude(),
						storeMap.get(eso.getGoodStoreId()).getLocation().getLat(),
						storeMap.get(eso.getGoodStoreId()).getLocation().getLon(),
						DistanceUnit.METERS);

				log.debug("商品:" + eso.getGoodsName() + "的距离是:" + distance);
			}
		}

		return page;
	}

	@Override
	public Page<EsGoods> queryGoodsByStore(StoreIdQueryVo storeIdQueryVo, String storeId) {

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		// 店铺id
		// 如果店铺ID不为空则表示查询其它店铺商品，只允许查询已上架商品
		// 为空则表示查询本店商品，则查询所有状态的商品
		if (StringUtils.isNotEmpty(storeId)) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("goodStoreId", storeId));
		}

		// 商品名
		if (StringUtils.isNotEmpty(storeIdQueryVo.getGoodsName())) {
			boolQueryBuilder
					.must(QueryBuilders.matchQuery("goodsName", storeIdQueryVo.getGoodsName()));
		}

		// 分类id
		if (StringUtils.isNotEmpty(storeIdQueryVo.getGcId())) {
			boolQueryBuilder.must(QueryBuilders.matchQuery("gcId", storeIdQueryVo.getGcId()));
		}

		// 排序
		if (StringUtils.isEmpty(storeIdQueryVo.getSort())
				&& StringUtils.isEmpty(storeIdQueryVo.getOrder())) {
			SortBuilder sortBuilder;
			if (StringUtils.equalsIgnoreCase(storeIdQueryVo.getSort(), "desc")) {
				sortBuilder = SortBuilders.fieldSort(storeIdQueryVo.getOrder()).sortMode("desc");
			} else {
				sortBuilder = SortBuilders.fieldSort(storeIdQueryVo.getOrder()).sortMode("aes");
			}
			nativeSearchQueryBuilder.withSort(sortBuilder);
		}

		// 查询
		nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

		return esGoodsRepository.search(nativeSearchQueryBuilder.build());
	}

	private DemoGoods dsGoods2DemoGoods(DsGoods dsGoods) {

		DemoGoods demoGoods = new DemoGoods();
		demoGoods.setGoodsDesc(dsGoods.getGoodsDesc());
		demoGoods.setGoodsName(dsGoods.getGoodsName());
		demoGoods.setGoodsPrice(dsGoods.getGoodsPrice().doubleValue());
		demoGoods.setShopId(dsGoods.getStoreId());
		demoGoods.setGoodsId(dsGoods.getId());
		demoGoods.setAppId(dsGoods.getAppId());

		return demoGoods;
	}
}
