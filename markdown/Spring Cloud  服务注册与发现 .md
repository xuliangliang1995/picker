# Spring Cloud 服务注册与发现（Eureka）

![springCloud.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200109/fdb7176479adaaa41d4334cea3cc091f.png_target)

演示代码 `github` 地址：[https://github.com/xuliangliang1995/spring-cloud-study](https://github.com/xuliangliang1995/spring-cloud-study)
## Eureka Server

首先，我们需要先启动一个 `EurekaServer` 注册中心。

### 1.引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
### 2. @EnableEurekaServer
通过 `@EnableEurekaServer` 来开启 `EurekaServer` 配置。

```java
@SpringBootApplication
@EnableEurekaServer
public class Sb002EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sb002EurekaServerApplication.class, args);
    }

}
```
### 3. application.properties 
```python
server.port=10001
spring.application.name=eureka-server
# 把当前实例（自己）也注册在 EurekaServer
eureka.client.service-url.defaultZone=http://localhost:${server.port}/eureka/
```

### 4. 运行 BootStrap 类
在浏览器打开：[http://localhost:10001](http://localhost:10001) 即可查看 `EurekaServer` 页面。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200111/f586425b9f40469f2c3130aa0df38804.png_target)

在图中可以看到 Instances currently registered with Eureka 一栏，即当前注册到该 Eureka 的实例。
图中的 `xuliangdembp.lan:eureka-server:10001` 即是我们启动的 EurekaServer 自身。`EUREKA-SERVER`即我们配置的`spring.application.name`。
当然，打开页面时，也可能并没有发现注册实例。那是因为第一次注册的时候，我们的 `EurekaServer` 还没有完全启动，导致没能注册上，耐心等待 30 秒左右，再次刷新页面，应该就能看到注册实例（如果代码没有问题的话）。

## EurekaClient
### 1. 引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
### 2. @EnableEurekaClient
通过 `@EnalbeEurekaClient` 开启 Eureka 客户端配置。
```java
@SpringBootApplication
@EnableEurekaClient
public class Sb002EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sb002EurekaClientApplication.class, args);
    }

}
```
### 3. application.properties
```python
server.port=10002
spring.application.name=eureka-client
eureka.client.service-url.defaultZone=http://localhost:10001/eureka/
```
`eureka.client` 配置参考的是`EurekaClientConfigBean` 这个类。该类内容主要如下：

```java
@ConfigurationProperties("eureka.client")
public class EurekaClientConfigBean implements EurekaClientConfig, Ordered {
    public static final String PREFIX = "eureka.client";
    public static final String DEFAULT_URL = "http://localhost:8761/eureka/";
    public static final String DEFAULT_ZONE = "defaultZone";
    private static final int MINUTES = 60;
    @Autowired(
        required = false
    )
    PropertyResolver propertyResolver;
    private boolean enabled = true;
    @NestedConfigurationProperty
    private EurekaTransportConfig transport = new CloudEurekaTransportConfig();
    private int registryFetchIntervalSeconds = 30;
    private int instanceInfoReplicationIntervalSeconds = 30;
    private int initialInstanceInfoReplicationIntervalSeconds = 40;
    private int eurekaServiceUrlPollIntervalSeconds = 300;
    private String proxyPort;
    private String proxyHost;
    private String proxyUserName;
    private String proxyPassword;
    private int eurekaServerReadTimeoutSeconds = 8;
    private int eurekaServerConnectTimeoutSeconds = 5;
    private String backupRegistryImpl;
    private int eurekaServerTotalConnections = 200;
    private int eurekaServerTotalConnectionsPerHost = 50;
    private String eurekaServerURLContext;
    private String eurekaServerPort;
    private String eurekaServerDNSName;
    private String region = "us-east-1";
    private int eurekaConnectionIdleTimeoutSeconds = 30;
    private String registryRefreshSingleVipAddress;
    private int heartbeatExecutorThreadPoolSize = 2;
    private int heartbeatExecutorExponentialBackOffBound = 10;
    private int cacheRefreshExecutorThreadPoolSize = 2;
    private int cacheRefreshExecutorExponentialBackOffBound = 10;
    private Map<String, String> serviceUrl = new HashMap();
    private boolean gZipContent;
    private boolean useDnsForFetchingServiceUrls;
    private boolean registerWithEureka;
    private boolean preferSameZoneEureka;
    private boolean logDeltaDiff;
    private boolean disableDelta;
    private String fetchRemoteRegionsRegistry;
    private Map<String, String> availabilityZones;
    private boolean filterOnlyUpInstances;
    private boolean fetchRegistry;
    private String dollarReplacement;
    private String escapeCharReplacement;
    private boolean allowRedirects;
    private boolean onDemandUpdateStatusChange;
    private String encoderName;
    private String decoderName;
    private String clientDataAccept;
    private boolean shouldUnregisterOnShutdown;
    private boolean shouldEnforceRegistrationAtInit;
    private int order;

    public EurekaClientConfigBean() {
        this.serviceUrl.put("defaultZone", "http://localhost:8761/eureka/");
        this.gZipContent = true;
        this.useDnsForFetchingServiceUrls = false;
        this.registerWithEureka = true;
        this.preferSameZoneEureka = true;
        this.availabilityZones = new HashMap();
        this.filterOnlyUpInstances = true;
        this.fetchRegistry = true;
        this.dollarReplacement = "_-";
        this.escapeCharReplacement = "__";
        this.allowRedirects = false;
        this.onDemandUpdateStatusChange = true;
        this.clientDataAccept = EurekaAccept.full.name();
        this.shouldUnregisterOnShutdown = true;
        this.shouldEnforceRegistrationAtInit = false;
        this.order = 0;
    }
    ...
}
```
### 4. 启动 BootStrap 类
启动成功后再次访问 `EurekaServer`。[http://localhost:10001](http://localhost:10001)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200111/945a1ac8a283785ceea1c0d510b13da2.png_target)

发现 `EUREKA-CLIENT` 已经注册上来。`EUREKA-CLIENT` 即我们配置的 `spring.application.name`。

### 5. DiscoveryClient
我们可以通过 `DiscoveryClient` 对象来打印所有服务。
```java
public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(Sb002EurekaClientApplication.class, args);
    DiscoveryClient discoveryClient = ctx.getBean(DiscoveryClient.class);
    discoveryClient.getServices().forEach(s -> System.out.println(s));
}
```
重新启动，即可看到控制台打印出所有服务。
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200111/7feaf1273ae0f139997b6f4f37945d1d.png_target)

## Eureka Server 集群
`eureka-server`集群很简单，只需要多启动几个应用，然后修改配置如下：
```python
eureka.client.service-url.defaultZone=http://localhost:10001/eureka/,http://localhost:10000/eureka/
```
`defaultZone` 列出所有注册中心地址即可。用 `,` 分隔。运行多个注册中心后，任意打开一个都可以看到所有已注册的注册中心实例。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200123/c2b0fa75e7f1eb3e9c23088eb217a7d6.png_target)