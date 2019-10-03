# picker
### 初学分布式 —— 练习项目
* [program](https://github.com/xuliangliang1995/picker/tree/master/program) // 项目根目录
  * [picker-parent](https://github.com/xuliangliang1995/picker/tree/master/program/picker-parent)  // 依赖管理
  * [picker-commons](https://github.com/xuliangliang1995/picker/tree/master/program/picker-commons)
    * [commons-core](https://github.com/xuliangliang1995/picker/tree/master/program/picker-commons/commons-core) // 定义了公共常量和规范
    * [commons-multidb](https://github.com/xuliangliang1995/picker/tree/master/program/picker-commons/commons-multidb) // 多数据源配置公共模块
      * [README.md](https://github.com/xuliangliang1995/picker/blob/master/program/picker-commons/commons-multidb/README.md) // 多数据源模块引入说明文档
    * [commons-tools](https://github.com/xuliangliang1995/picker/tree/master/program/picker-commons/commons-tools)	// 通用工具
  * [picker-service-user](https://github.com/xuliangliang1995/picker/tree/master/program/picker-service-user)
    * [user-service-api](https://github.com/xuliangliang1995/picker/tree/master/program/picker-service-user/user-service-api) // 用户服务 API
    * [user-service-provider](https://github.com/xuliangliang1995/picker/tree/master/program/picker-service-user/user-service-provider) // 用户服务 提供者
    * [user-service-sdk](https://github.com/xuliangliang1995/picker/tree/master/program/picker-service-user/user-service-sdk) // 用户服务 SDK
  * [picker-user](https://github.com/xuliangliang1995/picker/tree/master/program/picker-user) // web 
* [demo](https://github.com/xuliangliang1995/picker/tree/master/demo) // 简单 demo
  * [picker-zookeeper](https://github.com/xuliangliang1995/picker/tree/master/demo/picker-zookeeper) // zookeeper 分布式锁
  * [picker-kafka](https://github.com/xuliangliang1995/picker/tree/master/demo/picker-kafka) 
* [markdown](https://github.com/xuliangliang1995/picker/tree/master/markdown) // 部分环境搭建文档记录



## Nacos 控制台

http://nacos.grasswort.com:8848/nacos/index.html



## Nexus Maven 私服

[http://59.110.163.6:8081](http://59.110.163.6:8081/)



## Dubbo Admin 控制台

[http://59.110.163.6:8080](http://59.110.163.6:8080/)



## Zookeeper 集群

```properties
zookeeper://zookeeper.grasswort.com:2181
```



## Nacos 集群

```properties
nacos://nacos.grasswort.com:8848
```



## Kafka 集群

```shell
182.92.3.187:9092,182.92.160.62:9092,39.96.42.239:9092
```



