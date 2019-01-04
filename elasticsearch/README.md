# 改进的Elasticsearch java客户端使用例子
    在例子spring-data-elasticsearch的基础上修改
    还没完全完成
    
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
    
# 其他
    elasticsearch-analysis-ik-1.9.5依赖的ES版本是2.3.5
    springboot2.x的版本的starter要求ES版本最低5.0以上，而且自己配置很多问题;
        如果不使用starter，需要自己配置依赖和注册bean
    
# 备注
    还没有整合业务代码..
    还没合并距离操作的方法
    继承ElasticsearchRepository进行操作的例子还没写
    