# Mybatis 多数据源配置公共模块（使用示例）
1. 引入依赖
```xml
<dependency>
    <groupId>com.grasswort.picker</groupId>
    <artifactId>commons-multidb</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. 添加包的扫描
```java
@ComponentScan(basePackages = {
        "com.grasswort.picker.commons.config"
})
@SpringBootApplication
public class BootStrap {}
```
3. 添加配置文件(默认 DataSource 使用阿里的 DruidDataSource)
```yaml
multidb: 
    defaultGroup: MASTER
    wrappers: # 这是一个集合，当增加数据源的时候，只需要再增加一个配置即可
      - group: MASTER # 自己定义
        weight: 1      # 权重 1 ~ 10 之间
        dataSource:    # 数据源，可选择自己的数据源
          url: jdbc:mysql://114.67.99.146:3306/picker?useUnicode=true&characterEncoding=utf8&useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=UTC
          username: xuliang
          password: xol4l2y2xx
          driver-class-name: com.mysql.cj.jdbc.Driver
          type: com.alibaba.druid.pool.DruidDataSource
          initialSize: 2
          minIdle: 1
          maxActive: 20
          maxWait: 60000
          timeBetweenEvictionRunsMillis: 300000
          validationQuery: SELECT 1 FROM DUAL
          testWhileIdle: true
          testOnBorrow: false
          testOnReturn: false
          poolPreparedStatements: false
          maxPoolPreparedStatementPerConnectionSize: 20
          filters: stat,config
          connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
          useGlobalDataSourceStat: true
    
      - group: SLAVE
        weight: 1
        dataSource:
          url: jdbc:mysql://114.67.99.146:3306/picker?useUnicode=true&characterEncoding=utf8&useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=UTC
          username: xuliang
          password: xol4l2y2xx
          driver-class-name: com.mysql.cj.jdbc.Driver
          type: com.alibaba.druid.pool.DruidDataSource
          initialSize: 2
          minIdle: 1
          maxActive: 20
          maxWait: 60000
          timeBetweenEvictionRunsMillis: 300000
          validationQuery: SELECT 1 FROM DUAL
          testWhileIdle: true
          testOnBorrow: false
          testOnReturn: false
          poolPreparedStatements: false
          maxPoolPreparedStatementPerConnectionSize: 20
          filters: stat,config
          connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
          useGlobalDataSourceStat: true
```
4.注入 DataSource
```java
@Configuration
public class ConfigurationBean {
    
    /**
     * 注入 DataSource
     * 为了和 DataSource 区分开，使用 MultiDataSourceWrapper 封装了生成的 RoutingDataSource,调用 getDataSource() 即可获取动态数据源对象
     **/    
    @Bean
    public DataSource dataSource(@Autowired MultiDataSourceWrapper wrapper) {
        return wrapper.getDataSource();
    }

}
```
5. 使用示例
```java
public class RegisterService {
    @DB("MASTER") // 从 MASTER 组中取一个数据源 （区分大小写）
    public void register() {}
}
```
6. 其他
```java
@Configuration
public class ConfigurationClass {
        // 如果要使用其他数据源，由 new DataSourceWrapperList(OtherDataSource.class) 传入其他数据源 class
        @Bean
        @Primary
        public DataSourceWrapperList masterDataSourceWrapper() {
            return new DataSourceWrapperList(OtherDataSource.class);
        }
        
        // 将 DataSource 注入 SqlSessionFactory
        @Bean
        public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setMapperLocations(
                    new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_LOCATION));
            return sqlSessionFactoryBean.getObject();
        }
}
```