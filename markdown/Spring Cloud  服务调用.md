# SpringCloud 服务调用 — OpenFeign

![img.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200109/fdb7176479adaaa41d4334cea3cc091f.png_target)

`Feign` 是一个声明式的 Web 服务客户端。它使用起来非常的简单，创建一个接口，并对其添加注解（包括 `Feign` 注解 和 `JAX-RS` 注解，Spring Cloud 添加了对 `Spring MVC` 注解的支持。）即可。示例如下：

```java
@FeignClient("eureka-server")
public interface BookServiceClient {

    @RequestMapping(value = "books", method = RequestMethod.GET)
    List<Book> books();

    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    Book book(@PathVariable("bookId")Long bookId);

}
```

注： 该教程参考 `SpringCloudOpenFeign` [官方文档](https://cloud.spring.io/spring-cloud-openfeign/reference/html/)。

以下演示代码 `GitHub` 地址：[https://github.com/xuliangliang1995/spring-cloud-study](https://github.com/xuliangliang1995/spring-cloud-study)

## Spring Boot 集成 OpenFeign

`SpringBoot` 应用集成 `Feign`  非常简单，官方提供了固化的 `starter`，引入即可。

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

然后通过在启动配置类中添加`@EnableFeignClients` 注解来开启`FeignClients` 模块。`EnableFeignClients` 会引入 `FeignClientsRegistrar` 对标记了 `@FeignClients` 的类进行 `BeanDefinition` 注册。

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Sb002EurekaClientApplication {
  
    public static void main(String[] args) {
        SpringApplication.run(Sb002EurekaClientApplication.class, args);
    }
  
}
```

## 服务提供者

上一篇 [服务的注册与发现](https://grasswort.com/blog/05a2c71754b900b8.html) 中我们分别创建了 `eureka-server` 和 `eureka-client` 两个 SpringBoot 应用。现在呢，我们在 `eureka-server` 中添加如下代码，作为服务提供方。

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

```java
public class Book {

    private Long id;

    private String name;

    private String author;

    ... // 省略 Getter 和 Setter 方法。
}
```



## 服务调用方

那么，`eureka-client` 就作为我们的服务调用方咯。

尽管该篇是接着 [服务的注册与发现](https://grasswort.com/blog/05a2c71754b900b8.html) 来讲。但关键点依然会贴出来，所以可以独立查看。

1. 引入`spring-cloud-starter-openfeign` 依赖。

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
<!-- 新增的 OpenFeign starter -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

2. 在启动配置类 `Sb002EurekaClientApplication` 上添加 `@EnableDiscoveryClient`和`@EnableFeignClients` 注解。

```java
@SpringBootApplication
// @EnableEurekaClient 有了 @EnableDiscoveryClient, @EnableEurekaClient 可以省去。
@EnableDiscoveryClient
@EnableFeignClients
public class Sb002EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sb002EurekaClientApplication.class, args);
    }

}
```

3. 然后声明一个`BookServiceClient`接口，并添加 `@FeignClient` 注解。

```java
@FeignClient("eureka-server") // eureka-server 即服务提供者的`spring.application.name`
public interface BookServiceClient {

    @GetMapping("book")
    List<Book> books();

    @GetMapping("books/{bookId}")
    Book book(@PathVariable("bookId")Long bookId);

}
```

4. 然后再写一个 `BookController` 类来对 `BookServiceClient` 进行调用。

```java
@RestController
@RequestMapping("books")
public class BookController {

    @Autowired
    BookServiceClient bookServiceClient;

    @GetMapping
    public List<Book> books() {
        return bookServiceClient.books();
    }

    @GetMapping("/{bookId}")
    public Book book(@PathVariable("bookId") Long bookId) {
        return bookServiceClient.book(bookId);
    }
}
```

5. 为了省去翻看上篇的时间，这里再次提供 `application.yaml` 文件配置内容。

```yaml
spring:
  application:
    name: eureka-client
server:
  port: 10002
eureka:
  client:
    service-url:
      # http://localhost:10001/eureka/ 为 eureka-server 地址。
      defaultZone: http://localhost:10001/eureka/
```

6. 然后我们依次启动 `eureka-server` 和 `eureka-client` 两个 SpringBoot 应用。在浏览器访问：

   `http://localhost:10002/books`

   `http://localhost:10002/books/1`

   测试结果，成功返回：

   ```json
   [
       {
           "id": 1,
           "name": "本草纲目",
           "author": "李时珍"
       },
       {
           "id": 2,
           "name": "SpringBoot 编程思想",
           "author": "小马哥"
       }
   ]
   ```

   至此，一个简单的远程服务调用 Demo 便已完成。

   

## 覆盖默认配置

`Spring Cloud` 的 `Feign` 支持的核心概念是命名客户端。每个 `@FeignClient` 都是组件集成的一部分。它与 `feign.Decoder`，`feign.Encoder` 和 `feign.Contract` 等共同组合成一个新的组件集 `FeignClientsConfiguration`。

`Spring Cloud Netflix` 默认为 `Feign` 提供以下 `Bean`  :

| BeanType        | beanName        | ClassName                |
| --------------- | --------------- | ------------------------ |
| `Decoder`       | `feignDecoder`  | `ResponseEntityDecoder`  |
| `Encoder`       | `feignEncoder`  | `SpringEncoder`          |
| `Logger`        | `feignLogger`   | `Slf4jLogger`            |
| `Contract`      | `feignContract` | `SpringMvcContract`      |
| `Feign.Builder` | `feignBuilder`  | `HystrixFeign.Builder`   |
| `Client`        | `feignClient`   | `条件装配（见表格下方）` |

* 如果 `Ribbon` 在类路径中，且 `spring.cloud.loadbalancer.ribbon.enabled = true` 则使用 `LoadBalancerFeignClient`。否则， 如果 `Spring Cloud LoadBalancer` 在类路径中， 则使用 `FeignBlockingLoadBalancerClient`。如果它们都不在类路径中，则使用默认的 feign client。

默认情况下，`Spring Cloud Netflix`  不会为 `Feign` 提供以下 bean， 但仍会从应用程序上下文中根据类型查找使用。

* `Logger.level`

* `Retryer`

* `ErrorDecoder`

* `Request.Options`

* `Collection<RequestInterceptor>`

* `SetterFactory`

* `QueryMapEncoder`

  

1. 在 `@FeignClient`  注解中，我们可以通过 `configuration` 属性来覆盖默认配置。例如：

```java
@FeignClient(name = "eureka-server", configuration = FooConfiguration.class)
public interface BookServiceClient {

    @RequestMapping(value = "books", method = RequestMethod.GET)
    List<Book> books();

    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    Book book(@PathVariable("bookId")Long bookId);

}
```

```java
// @Configuration
// 注意，这里是不需要 @Configuration 的，加上后，会覆盖所有的 FeignClient 配置。
public class FooConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /// SpringCloudOpenFeign 默认使用的是 SpringMvcContract
  	/// 这里修改成了 feign.Contract.Default()。该 Contract 是不支持 SpringMVC 注解的。 而是支持 @RequestLine 注解
    /// 使用 SpringMvc 注解 @RequestMapping 会报错 Method book not annotated with HTTP method type (ex. GET, POST)
    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }
}
```

2. 可以通过 `@EnableFeignClients` 的 `defaultConfiguration` 来设置全局默认配置。

```java
@EnableFeignClients(defaultConfiguration = FooConfiguration.class)
```

3. `@FeignClient` 也可以通过属性文件进行配置。此处优先级默认高于 `@Configuration bean` 。如需更改优先级，可以设置 `feign.client.default-to-properties` 为 `false`。

```yaml
feign:
  client:
    config:
      # eureka-server 为 FeignClient name, 也可以改成 default 来修改全局默认配置。
      eureka-server: 
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: full
        errorDecoder: com.example.SimpleErrorDecoder
        retryer: com.example.SimpleRetryer
        requestInterceptors:
          - com.example.FooRequestInterceptor
          - com.example.BarRequestInterceptor
        decode404: false
        encoder: com.example.SimpleEncoder
        decoder: com.example.SimpleDecoder
        contract: com.example.SimpleContract
```



## OkHttpClient 与 ApacheHttpClient

OkHttpClient和ApacheHttpClient伪装客户端可以通过分别将`feign.okhttp.enabled`或设置 `feign.httpclient.enabled` 为来使用 `true` ，并将它们放在类路径中。您可以通过`org.apache.http.impl.client.CloseableHttpClient` 在使用Apache或 `okhttp3.OkHttpClient` 使用OK HTTP 时提供Bean来定制所使用的HTTP客户端。



## Feign Hystrix 支持

`hystrix` 类似一个断路器，用于执行当服务熔断降级或发生异常后的回退逻辑。类似于 `dubbo` 中的 `mock`。启用 `hystrix` 的条件：

1. `Hystrix` 在 `classpath` 下。（`starter-openfeign` 本身就包含 `hystrix`）
2. `feign.hystrix.enabled=true`

禁用 `hystrix`：

```java
@Configuration
public class FooConfiguration {
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
```

上文提到 `Feign.builder` 默认实现为 `HystrixFeign.Builder`。这里修改为 `Feign.builder`。 自然就禁用啦。

`hystrix` 有两种实现方式：

1. 指定 `@FeignClient` 的 `fallback` 属性：

```java
@FeignClient(contextId = "book", name = "eureka-server", fallback = BookServiceClientHystrixFallback.class)
public interface BookServiceClient {

    @RequestMapping(value = "books", method = RequestMethod.GET)
    List<Book> books();

    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    Book book(@PathVariable("bookId")Long bookId);

}
```

```java
@Component
public class BookServiceClientHystrixFallback implements BookServiceClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Book> books() {
        logger.warn("books request fail !");
        return Collections.EMPTY_LIST;
    }

    @Override
    public Book book(Long bookId) {
        logger.warn("books/{} request fail !", bookId);
        return null;
    }
}
```

2. 指定 `@FeignClient` 的 `fallbackFactory` 属性：

```java
@FeignClient(contextId = "book", name = "eureka-server", fallbackFactory = BookServiceClientHystrixFallbackFactory.class)
public interface BookServiceClient {

    @RequestMapping(value = "books", method = RequestMethod.GET)
    List<Book> books();

    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    Book book(@PathVariable("bookId")Long bookId);

}
```

```java
@Component
public class BookServiceClientHystrixFallbackFactory implements FallbackFactory<BookServiceClient> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BookServiceClient create(Throwable cause) {
        return new BookServiceClient() {
            @Override
            public List<Book> books() {
                logger.warn("books request fail ! cause reason: {}", cause.getMessage());
                return Collections.EMPTY_LIST;
            }

            @Override
            public Book book(Long bookId) {
                logger.warn("books/{} request fail ! cause reason: {}", bookId, cause.getMessage());
                return null;
            }
        };
    }
}
```

`hystrix` 超时时间配置：
```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          #strategy: SEMAPHORE
          thread:
            timeoutInMilliseconds: 10000
```



## Feign 请求/响应压缩

```python
feign.compression.request.enabled=true
feign.compression.request.mime-types=text/xml,application/xml,application/json
feign.compression.request.min-request-size=2048
feign.compression.response.enabled=true
feign.compression.response.useGzipDecoder=true
```



## Hateoas 支持

如果项目使用了 `org.springframework.boot:spring-boot-starter-hateoas` 或 `org.springframework.boot:spring-boot-starter-data-rest` 启动器，则默认开启 `hateoas` 支持。

启用 `HATEOAS` 支持后，允许 `FeignClient` 序列化和反序列化HATEOAS表示模型。如：`EntityModel`，`CollectionModel` 和 `PagedModel`。例如：

```java
@FeignClient("demo")
public interface DemoTemplate {

    @GetMapping(path = "/stores")
    CollectionModel<Store> getStores();
}
```

## 负载均衡
`Feign` 结合 `Ribbon` 的负载均衡配置可以看下篇 [Spring Cloud 负载均衡](https://grasswort.com/blog/2b0678e43baae629.html)。