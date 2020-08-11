# Spring Cloud Config 配置中心

![springCloud.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200109/fdb7176479adaaa41d4334cea3cc091f.png_target)

演示代码 `gitHub` 地址：[https://github.com/xuliangliang1995/spring-cloud-study](https://github.com/xuliangliang1995/spring-cloud-study)
下面呢，我们会创建两个 `SpringBoot` 应用。一个是 `config-server` 作为配置中心（基于 `git` 仓库）。另一个是 `config-client`，作为客户端来使用配置中心的配置。

## Config Server - Git

### 1. 引入依赖
配置中心主要依赖于 `config-server` 和 `web starter`。
推荐使用 `Spring Initializr` 或 `start.spring.io` 来生成项目。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

`SpringCloud` 依赖管理通常是集成父项目 `spring-cloud-starter-parent` 或者导入 `spring-cloud-dependencies` 依赖配置。这里采用第二种。版本管理通过 `scope = import` 引入。

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. @EnableConfigServer 

```java
@SpringBootApplication
@EnableConfigServer
public class Sb001ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Sb001ConfigServerApplication.class, args);
    }
}
```

### 3. 创建 Git 配置文件仓库

在 `github` 仓库根目录（不强制一定是根目录里，只是此处选择了根目录）下创建了两个文件：
1. `demo-dev.properties`
2. `demo-pro.properties`

文件内容为：

```python
message = [dev/pro] hello world !
```

配置文件可选格式如下，这里选择`application-profile.properties`。
即： `demo` 为客户端 `spring.application.name`。 `dev` 为 客户端 `spring.cloud.config.profile`。

```python
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```


### 4. 配置 application.properties

```python
spring.application.name = config-server
server.port=8888
spring.cloud.config.server.git.uri = https://github.com/xuliangliang1995/gitConfigurationStorage.git
spring.cloud.config.server.git.username = USER_NAME
spring.cloud.config.server.git.password = PASSWORD
```

配置内容参考 `MultipleJGitEnvironmentProperties` 类。该类继承自 `AbstractScmAccessorProperties`。配置文件查询路径为：`basedir + searchPaths`,`searchPaths` 为数组，可以配置多个。
```java
public class AbstractScmAccessorProperties implements EnvironmentRepositoryProperties {
    static final String[] DEFAULT_LOCATIONS = new String[]{"/"};
    private String uri;
    private File basedir;
    private String[] searchPaths;
    private String username;
    private String password;
    private String passphrase;
    private boolean strictHostKeyChecking;
    private int order;
    private String defaultLabel;

    public AbstractScmAccessorProperties() {
        this.searchPaths = (String[])DEFAULT_LOCATIONS.clone();
        this.strictHostKeyChecking = true;
        this.order = 2147483647;
    }
    ...
}
```
5. 启动 `config-server` 应用
GET 访问： `http://localhost:8888/demo/dev`
```json
http://localhost:8888/demo/dev
{
    "name": "demo",
    "profiles": [
        "dev"
    ],
    "label": null,
    "version": "80e9299c4bb62d7d755306e6d88aaf412d486d1c",
    "state": null,
    "propertySources": [
        {
            "name": "https://github.com/xuliangliang1995/gitConfigurationStorage.git/demo-dev.properties",
            "source": {
                "message": "[dev]Hello world !"
            }
        }
    ]
}
```

## Config Client

### 1. 依赖引入
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
如果需要动态刷新，需要引入`actuator`依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 2. 配置 `bootstrap.properties`

配置服务信息需要写在 `bootstrap.properties` 配置文件中。

```python
spring.application.name=demo
spring.cloud.config.uri=http://127.0.0.1:8888
spring.cloud.config.profile=dev
```
该配置依据`ConfigurationProperties`类。
```java
@ConfigurationProperties("spring.cloud.config")
public class ConfigClientProperties {
    public static final String PREFIX = "spring.cloud.config";
    public static final String TOKEN_HEADER = "X-Config-Token";
    public static final String STATE_HEADER = "X-Config-State";
    public static final String AUTHORIZATION = "authorization";
    private boolean enabled = true;
    private String profile = "default";
    @Value("${spring.application.name:application}")
    private String name;
    private String label;
    private String username;
    private String password;
    private String[] uri = new String[]{"http://localhost:8888"};
    private ConfigClientProperties.Discovery discovery = new ConfigClientProperties.Discovery();
    private boolean failFast = false;
    private String token;
    private int requestReadTimeout = 185000;
    private int requestConnectTimeout = 10000;
    private boolean sendState = true;
    private Map<String, String> headers = new HashMap();

    private ConfigClientProperties() {
    }
    ...
}
```

### 3. 配置 application.properties
默认 `/actuator/refresh` 是不暴露的，可以通过如下配置暴露 all endpoints, 也可以只暴露 `refresh`。

```python
# exposure all endpoints
management.endpoints.web.exposure.include=*
```
### 4. 写一个 `HelloController`
```java
@RestController
@RefreshScope //必须要有该注解
public class HelloController {

    @Value("${message:defaultMessage}")
    private String message;

    @GetMapping
    public String hello() {
        return message;
    }
}
```
### 5. 启动 config client
GET 访问 
`http://localhost:8080/`

```python
[dev]Hello world !
```
### 6. 更改配置并刷新

修改 `demo-dev.properties`:
```python
message = [dev]Hello world v2 !
```
再次访问 
`http://localhost:8080/`

```python
[dev]Hello world !
```
结果不变，我们需要调用`refresh`来刷新配置。

POST: 
`http://localhost:8080/actuator/refresh`

```json
[
    "config.client.version",
    "message"
]
```
结果返回了变更的属性。

再次 GET： 
`http://localhost:8080/`

```python
[dev]Hello world v2 !
```