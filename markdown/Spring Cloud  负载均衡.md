# Spring Cloud 负载均衡 - Ribbon
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200109/fdb7176479adaaa41d4334cea3cc091f.png_target)

`Ribbon` 是一套实现相对简单的客户端负载均衡的开源框架。

上一篇 [Spring Cloud 远程服务调用](https://grasswort.com/blog/adb6b3f7e102a60a) 写了通过 `Spring Cloud OpenFeign` 来进行远程服务调用。

参考文章：
1. [spring cloud 官方文档](https://projects.spring.io/spring-cloud/spring-cloud.html#spring-cloud-ribbon)
2. [Client Side Load Balancing with Ribbon and Spring Cloud](https://spring.io/guides/gs/client-side-load-balancing/)

以下演示代码 `GitHub` 地址：[https://github.com/xuliangliang1995/spring-cloud-study](https://github.com/xuliangliang1995/spring-cloud-study)

## 开始前的准备
我们需要有一个服务提供者。假设我们有一个 `eureka-server` 的应用，并提供了以下服务。(如果没有，可以阅读之前的一篇 [服务注册与发现](https://grasswort.com/blog/05a2c71754b900b8))
```java
@RestController
@RequestMapping("books")
public class BookServiceController {

    private static final List<Book> BOOK_LIST = new LinkedList<>(Arrays.asList(
            new Book(1L, "本草纲目", "李时珍"),
            new Book(2L, "SpringBoot 编程思想", "小马哥")
    ));

    @GetMapping
    public List<Book> books() {
        return BOOK_LIST;
    }

    @GetMapping("/{bookId}")
    public Book book(@PathVariable("bookId") Long bookId) {
        return BOOK_LIST.stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst()
                .orElse(null);
    }
}
```

## 引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```
## 开启负载均衡
注意，这步仅针对使用 `RestTemplate` 来进行远程调用的方式。如果使用的是 `Feign` 则不需要这步。这一步非常简单，通过在 `RestTemplate` 上添加 `@LoadBalanced` 注解即可开启负载均衡。

```java
@Bean
@LoadBalanced // 为 restTemplate 开启负载均衡
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```
## @LoadBalanced 为什么可以开启负载均衡

首先，我们要知道 `@LoadBalanced` 是 `@Qualifier` 的派生注解。

```java
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Qualifier
public @interface LoadBalanced {
}
```

然后再看这个 `LoadBalancerAutoConfiguration` 类。

```java
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnClass({RestTemplate.class})
@ConditionalOnBean({LoadBalancerClient.class})
@EnableConfigurationProperties({LoadBalancerRetryProperties.class})
public class LoadBalancerAutoConfiguration {
    @LoadBalanced
    @Autowired(
        required = false
    )
    // 这个 List 上标有 @LoadBalanced , 只有标注了 @LoadBalanced 的 RestTemplate 才会被注入到 `restTemplates` 中。
    // 这里的 restTemplate 会在下面的方法里添加一层负载拦截器拦截器（包含了负载均衡器 loadBalancerClient）
    private List<RestTemplate> restTemplates = Collections.emptyList();
    ... // 省略部分代码

    @Configuration(
        proxyBeanMethods = false
    )
    @ConditionalOnMissingClass({"org.springframework.retry.support.RetryTemplate"})
    static class LoadBalancerInterceptorConfig {
        LoadBalancerInterceptorConfig() {
        }

        @Bean
        // 负载均衡拦截器，构造方法里会传入一个 loadBalancerClient
        public LoadBalancerInterceptor ribbonInterceptor(LoadBalancerClient loadBalancerClient, LoadBalancerRequestFactory requestFactory) {
            return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        public RestTemplateCustomizer restTemplateCustomizer(final LoadBalancerInterceptor loadBalancerInterceptor) {
            return (restTemplate) -> {
                List<ClientHttpRequestInterceptor> list = new ArrayList(restTemplate.getInterceptors());
                // 标注了 @LoadBalanced 的 RestTemplate 会添加一个负载均衡拦截器
                list.add(loadBalancerInterceptor);
                restTemplate.setInterceptors(list);
            };
        }
    }
}
```
## 负载均衡器的默认配置

`Ribbon` 默认负载均衡器 `ILoadBalancer` 为 `ZoneAwareLoadBalancer`。
由以下几部分构成，`ribbon` 为它们提供了默认实现：

| 类                         | 实现                             |
| -------------------------- | -------------------------------- |
| `IClientConfig`            | `DefaultClientConfigImpl`        |
| `IRule`                    | `ZoneAvoidanceRule`              |
| `IPing`                    | `NoOpPing`                       |
| `ServerList<Server>`       | `ConfigrationBasedServerList`    |
| `ServerListFilter<Server>` | `ZonePreferenceServerListFilter` |

但如果使用了 `eureka`，则部分配置会被 `Ribbon` 覆盖：

| 类                   | 实现                             |
| -------------------- | -------------------------------- |
| `IPing`              | `NIWSDiscoveryPing `             |
| `ServerList<Server>` | `DiscoveryEnabledNIWSServerList` |

## 覆盖默认配置（全局）

更改默认负载均衡规则非常的简单，注入一个 `IRule` 就会覆盖掉默认的 `ZoneAvoidanceRule`。

```java
@Bean
public IRule ribbonRule() {
    return new RoundRobinRule();
    //return new RandomRule();
}
```
这种方式类似于下文提到的通过 `@RibbonClients` 的 `defaultConfiguration` 属性来指定全局默认配置。但还是略有不同，等同于下文写的 `Ribbon Client` 配置文件加了 `@Configuration` 注解且可以被扫描到的那种情况。

## 注解方式更改配置

`@RibbonClient` 和 `@RibbonClients`。

首先，我们看一下这两个注解的代码。

```java
@Configuration(
    proxyBeanMethods = false
)
@Import({RibbonClientConfigurationRegistrar.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RibbonClient {
    String value() default "";

    String name() default "";

    Class<?>[] configuration() default {};
}
```
```java
@Configuration(
    proxyBeanMethods = false
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RibbonClientConfigurationRegistrar.class})
public @interface RibbonClients {
    RibbonClient[] value() default {};

    Class<?>[] defaultConfiguration() default {};
}
```
这两个注解都 `import` 了一个  `ImportBeanDefinitionRegistrar` 类。即：`RibbonClientConfigurationRegistrar`。这个类是用来条件注册 `BeanDefinition` 的，里面有一个重要的方法：`registerBeanDefinitionRegistry(...)`。 它依次解析了 `@RibbonClients` 和  `@RibbonClient` 注解。阅读代码可知：
（1）`@RibbonClients` 是用来做全局默认配置的
（2）`@RibbonClient` 是用来为单个服务做个性配置的
```java
public class RibbonClientConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
    public RibbonClientConfigurationRegistrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(RibbonClients.class.getName(), true);
        if (attrs != null && attrs.containsKey("value")) {
            AnnotationAttributes[] clients = (AnnotationAttributes[])((AnnotationAttributes[])attrs.get("value"));
            AnnotationAttributes[] var5 = clients;
            int var6 = clients.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                AnnotationAttributes client = var5[var7];
                this.registerClientConfiguration(registry, this.getClientName(client), client.get("configuration"));
            }
        }

        if (attrs != null && attrs.containsKey("defaultConfiguration")) {
            String name;
            if (metadata.hasEnclosingClass()) {
                name = "default." + metadata.getEnclosingClassName();
            } else {
                name = "default." + metadata.getClassName();
            }

            this.registerClientConfiguration(registry, name, attrs.get("defaultConfiguration"));
        }

        Map<String, Object> client = metadata.getAnnotationAttributes(RibbonClient.class.getName(), true);
        String name = this.getClientName(client);
        if (name != null) {
            this.registerClientConfiguration(registry, name, client.get("configuration"));
        }

    }
    ... // 省略部分代码
}
```
所以，我们的负载均衡应该这样配置。
```java
// eureka-server 为服务名称，即服务提供方的 spring.application.name
@RibbonClient(name = "eureka-server", configuration = EurekaServerRibbonConfiguration.class)
```
或（和）
```java
@RibbonClients(defaultConfiguration = EurekaServerRibbonConfiguration.class)
```
或
```java
@RibbonClients(
        value = {
          @RibbonClient(name = "eureka-server", configuration = EurekaServerRibbonConfiguration.class)
        },
        defaultConfiguration = RibbonClientsConfiguration.class)
```
```java
// @Configuration 这里不需要添加 @Configuration 注解。
// 如果添加了 @Configuration 需要排除在 @ComponentScan 之外，否则会被其他 RibbonClient 共享。
public class EurekaServerRibbonConfiguration {
    @Autowired
    IClientConfig iClientConfig;

    // ribbon 默认负载均衡策略为 com.netflix.loadbalancer.ZoneAwareLoadBalancer
    @Bean
    public IRule ribbonRule(IClientConfig config) {
        // 通过在配置类中声明新的 bean 来覆盖掉默认配置。
        return new RandomRule();
    }
}
```

## 属性配置文件更改配置
**注**：经个人测试，这种配置可以生效，但是优先级极低，暂不明原由。

官方文档里面有这样一段话：
> You can configure some bits of a Ribbon client using external properties in <client>.ribbon.*, which is no different than using the Netflix APIs natively, except that you can use Spring Boot configuration files. The native options can be inspected as static fields in CommonClientConfigKey (part of ribbon-core).

所以我们可以通过下面的方式配置：

```python
# 负载均衡配置
eureka-server:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```
`eureka-server` 为服务名。
`NFLoadBalancerRuleClassName` 是指定负载均衡规则的实现类。这个属性来自 `CommonClientConfigKey` 这个类，点开可以查看更多配置。

## 测试
这样呢，负载均衡配置就已经结束。下面可以写一个 `Controller` 类来进行调用验证。
```java
@RestController
@RequestMapping("/v2/books")
public class BookControllerV2 {

    private final RestTemplate restTemplate;

    public BookControllerV2(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{bookId}")
    public Book books(@PathVariable("bookId")Long bookId) {
        Book book = restTemplate.getForObject("http://eureka-server/books/".concat(String.valueOf(bookId)), Book.class);
        return book;
    }
}
```
本人是通过切换轮询和随机两种策略来观察是否生效。具体验证过程不写在此处。