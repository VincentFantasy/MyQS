# 说明
    在例子spring-data-elasticsearch的基础上修改
    
    基本无任何配置，部分连接信息在application.properties中配置
    
    包、类说明:
    .com.example.elasticsearch.repository.ElasticsearchRepository是Elasticsearch操作客户端的接口，用这个注入来操作；
        具体实现类ElasticsearchRepositoryImpl（使用的是client进行操作，可以在非spring项目中使用）
        具体实现类ElasticsearchByTemplateRepositoryImpl（还没测试）
    .com.example.elasticsearch.config.ElasticsearchConfig为配置类，注册bean后可以导入
    .包com.example.elasticsearch.pojo.es存放操作所需的实体类
    .包com.example.elasticsearch.constant存放所需常量
    .com.example.elasticsearch.annotation.EnableMyElasticsearch注解，模仿springboot的Enable*注解，
        直接导入com.example.elasticsearch.config.ElasticsearchConfig，以后可以在springboot启动类上加入这个注解，
        无需配置
    .test包中有写好简单的测试用例
    
# 例子
    使用spring-data方式操作的，看com.example.elasticsearch下
        esentity是es的实体类
            @Document(indexName = "demo", type = "goods")指定了索引名，文档类型，此注解甚至可以设置分配，索引存储方式等
            @Id注明id，建议实体id和Es的id分开，es自己建id的方式更加高效
            @Field注明属性，不分析的字段一定要加index = FieldIndex.not_analyzed，因为默认会分析，加了此注解后，建议手动
            指定type，不然报错
            @JSONField注解可以不加，只是序列化成json时指定字段名
        esdao是spring data elasticsearch的repository方式
            类似于jpa，继承ElasticsearchRepository，指定泛型实体和id就行，接口写法类似jpa，会自动实现
        geo地理位置操作与复杂的动态查询，用继承ElasticsearchRepository的方式是难以实现的
            看 GoodsService的queryLocationRange(double lat, double lon, int km)接口
            NativeSearchQueryBuilder用于组合各种Builder，包括地位位置查询，直接使用BoolQueryBuilder，如条件不是很多，
            可以直接使用BoolQueryBuilder
            以某坐标为中心点指定范围内搜索用GeoDistanceQueryBuilder，详情看例子
            根据距离排序的用GeoDistanceSortBuilder，详情看例子
         计算距离用GeoDistance.ARC.calculate方法，精度高计算慢，用GeoDistance.PLANE精度稍低计算快
            看 GoodsService的queryLocationRange(double lat, double lon, int km)接口实现，计算两点距离
         根据父文档的距离进行范围查询和排序，看GoodsService的queryGoodsByRef方法
            根据父（子）属性查询，hasParentQuery（hasChildQuery）
            根据父（子）属性排序，innerHis
    
# 其他
    elasticsearch-analysis-ik-1.9.5依赖的ES版本是2.3.5
    springboot2.x的版本的starter要求ES版本最低5.0以上，而且自己配置很多问题;
        如果不使用starter，需要自己配置依赖和注册bean
    
# 备注
    