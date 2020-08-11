# Nacos 服务注册和配置中心
![微信截图_20191110191624.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191110/6e80683a8dd42423eccd2e003cc05688.png_target)

## Nacos 部署
[Nacos 单机、集群部署](https://picker.grasswort.com/blog/bd3d9c3a7b373807.html)

本文通过一个相对完整的 demo 来演示整个流程。

## 演示项目结构
**github:** [https://github.com/xuliangliang1995/dubbo-nacos](https://github.com/xuliangliang1995/dubbo-nacos)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/a2ad19a73d2195dc6371431f08daa5c4.png_target)
* **pay-api** 支付服务定义
* **pay-client** 支付客户端
* **pay-service-provider** 支付服务提供者

## 1. 定义支付接口

新建一个 pay-api 模块，该模块定义了支付接口。供服务消费者使用。服务提供者来实现。

```java
public interface PayService {
    /**
     * 订单支付
     * @param order 订单信息
     * @return  订单支付结果
     */
    String payOrder(String order);
}
```

## 2. 服务实现&注册
新建一个 pay-service-provider 模块。来实现 pay-api 定义的支付接口。

* maven 依赖：(运行时发现 nacos-starter 0.2.3 有点小问题，对依赖做了细微调整)
```xml
    <!-- pay-api -->
    <dependency>
      <groupId>com.grasswort.picker</groupId>
      <artifactId>pay-api</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- spring-boot-starter -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- dubbo starter -->
    <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo-spring-boot-starter</artifactId>
      <version>2.7.1</version>
    </dependency>

    <!-- dubbo -->
    <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo</artifactId>
      <version>2.7.3</version>
    </dependency>

    <!-- nacos 服务注册发现 starter -->
    <dependency>
      <groupId>com.alibaba.boot</groupId>
      <artifactId>nacos-discovery-spring-boot-starter</artifactId>
      <version>0.2.3</version>
      <exclusions>
        <exclusion>
          <groupId>com.alibaba.nacos</groupId>
          <artifactId>nacos-client</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

    <!-- nacos -->
    <dependency>
      <groupId>com.alibaba.nacos</groupId>
      <artifactId>nacos-client</artifactId>
      <version>1.0.0</version>
      <exclusions>
        <exclusion>
          <groupId>com.alibaba.nacos</groupId>
          <artifactId>nacos-api</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>com.alibaba.nacos</groupId>
      <artifactId>nacos-api</artifactId>
      <version>1.1.3</version>
    </dependency>
```
* ```application.yml```
```yml
dubbo:
  application:
    # 应用名称
    name: dubbo-nacos-pay-provider
  scan:
    # dubbo 扫描包
    base-packages: com.grasswort.picker.service
  registry:
    # 注册中心地址
    address: nacos://192.168.150.101:8848?backup=192.168.150.102:8848,192.168.150.103:8848
    # 启动注册检查（设为 true 的话，如果注册失败将会导致启动报错）
    check: false
```
* 支付服务实现类 ```PayServiceImpl.java```
```java
package com.grasswort.picker.pay.service;

import com.grasswort.picker.pay.PayService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author xuliangliang
 * @Classname PayServiceImpl
 * @Description 支付服务
 * @Date 2019/11/27 13:43
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 5000)
public class PayServiceImpl implements PayService {
    /**
     * 订单支付
     *
     * @param order 订单信息
     * @return 订单支付结果
     */
    @Override
    public String payOrder(String order) {
        return order.concat("支付成功");
    }
}
```
* 启动类 ```PayProviderBootstrap.java```
```java
package com.grasswort.picker.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuliangliang
 * @Classname PayProviderBootstrap
 * @Description 服务提供者启动类
 * @Date 2019/11/27 13:38
 * @blame Java Team
 */
@SpringBootApplication
public class PayProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(PayProviderBootstrap.class, args);
    }
}
```

* 运行 PayProviderBootstrap.java

查看日志： 注册成功

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/bd98768a6571b14efee04201e07f8f04.png_target)

查看注册中心：注册成功

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/83552f237ade33376d5e5bdd398954bd.png_target)

## 3. 服务发现

新建 pay-client 模块，作为客户端去发现和调用服务。

* maven 依赖（和服务注册一模一样，多了个 web-starter）
```xml
    <!-- 支付接口 -->
    <dependency>
      <groupId>com.grasswort.picker</groupId>
      <artifactId>pay-api</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- web starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- dubbo starter -->
    <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo-spring-boot-starter</artifactId>
      <version>2.7.1</version>
    </dependency>

    <!-- dubbo -->
    <dependency>
      <groupId>org.apache.dubbo</groupId>
      <artifactId>dubbo</artifactId>
      <version>2.7.3</version>
    </dependency>

    <!-- nacos 服务发现 starter -->
    <dependency>
      <groupId>com.alibaba.boot</groupId>
      <artifactId>nacos-discovery-spring-boot-starter</artifactId>
      <version>0.2.3</version>
      <exclusions>
        <exclusion>
          <groupId>com.alibaba.nacos</groupId>
          <artifactId>nacos-client</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

    <!-- nacos -->
    <dependency>
      <groupId>com.alibaba.nacos</groupId>
      <artifactId>nacos-client</artifactId>
      <version>1.0.0</version>
      <exclusions>
        <exclusion>
          <groupId>com.alibaba.nacos</groupId>
          <artifactId>nacos-api</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```
* ```application.yml```
```yml
dubbo:
  application:
    name: pay-client
  scan:
    base-packages: com.grasswort.picker.pay.controller
  # 协议可不配置（默认就是 dubbo、20880）
  protocol:
    name: dubbo
    port: 20880
  registry:
    address: nacos://192.168.150.101:8848?backup=192.168.150.102:8848,192.168.150.103:8848
  consumer:
    # 启动检查，设置为 true 的话，没有提供者启动会报错
    check: false

# web 服务暴露端口，和 dubbo 无关
server:
  port: 10001
```
* ```PayController.java```
```java
package com.grasswort.picker.pay.controller;

import com.grasswort.picker.pay.PayService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname PayController
 * @Description 支付 API
 * @Date 2019/11/27 14:13
 * @blame Java Team
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference(version = "1.0")
    PayService payService;

    @GetMapping("/{order}")
    public String pay(@PathVariable("order") String order) {
        return payService.payOrder(order);
    }
}
```
* 启动类 ```PayClientBootStrap.java```
```java
package com.grasswort.picker.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuliangliang
 * @Classname PayClientBootStrap
 * @Description 支付客户端启动类
 * @Date 2019/11/27 14:13
 * @blame Java Team
 */
@SpringBootApplication
public class PayClientBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(PayClientBootStrap.class, args);
    }
}
```
* 运行启动类

查看 Nacos 控制台，消费者也注册上啦

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/854bfb13d285e00d8785a99cc4065865.png_target)

* 调用支付 api 

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/a1be35dd44ea225a08ac5fcf0b03f2c8.png_target)

OK， 成功。服务注册和发现到此结束。

## 配置中心

### 创建外部化配置文件
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/024e4459810d7969a5fb371f61f2be73.png_target)

新建一个 test.properties 配置

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/b281225cff7cab4f10d263aa6ff8388b.png_target)

### 引入外部化配置文件

* 修改启动类，添加 @NacosPropertySource

```java
@SpringBootApplication
@NacosPropertySource(dataId = "test.properties", autoRefreshed = true, groupId = "DEFAULT_GROUP")
public class PayProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(PayProviderBootstrap.class, args);
    }
}
```

* 修改支付服务提供类，引入外部配置属性

```java
/**
 * @author xuliangliang
 * @Classname PayServiceImpl
 * @Description 支付服务
 * @Date 2019/11/27 13:43
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 5000)
public class PayServiceImpl implements PayService {
    // 引用成功则为 alipay, 失败为 wechat
    @NacosValue(value = "${test.pay.method:wechat}", autoRefreshed = true)
    private String payMethod;
    /**
     * 订单支付
     *
     * @param order 订单信息
     * @return 订单支付结果
     */
    @Override
    public String payOrder(String order) {
        return order.concat("支付成功").concat("【" + payMethod + "】");
    }
}
```
* 重新启动应用测试

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/f9101660ebc87e079f14fdf12493ac70.png_target)

* 因为我们配置了 autoRefreshed 为 true,修改外部配置 alipay 为 alipayv2
不用重启，重新调用接口

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/3438ef8740caf41af05c49bdb2926d9f.png_target)

OK， 我们发现返回内容已经发生了变化。

### application.yml 外部化配置

[官方文档](https://github.com/nacos-group/nacos-spring-boot-project/wiki/spring-boot-0.2.2-%E4%BB%A5%E5%8F%8A-0.1.2%E7%89%88%E6%9C%AC%E6%96%B0%E5%8A%9F%E8%83%BD%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C)

* maven 依赖

```xml
<!-- nacos 配置中心 -->
    <dependency>
      <groupId>com.alibaba.boot</groupId>
      <artifactId>nacos-config-spring-boot-starter</artifactId>
      <version>0.2.3</version>
    </dependency>
```

* 在 Nacos 控制台创建一个配置文件 pay-service-provider.yml。内容为 pay-service-provider 项目下的 application.yml 内容。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/2bae705619bd574b3c5056cbff8fbaac.png_target)

* application.yml
```yml
nacos:
  config:
    bootstrap:
      enable: true
      log:
        enable: false
    server-addr: nacos://192.168.150.101:8848?backup=192.168.150.102:8848,192.168.150.103:8848
    namespace: 714709d9-ff45-4e48-873f-f468f3dd90ad
    group: DEFAULT_GROUP
    data-id: pay-service-provider.yml
    type: yaml
    max-retry: 10
    auto-refresh: true
    config-retry-time: 3000
    config-long-poll-timeout: 46000
    enable-remote-sync-config: false

# 该内容已经转到了配置中心，注释掉，测试外部化配置是否生效
#dubbo:
#  application:
#    name: dubbo-nacos-pay-provider
#  scan:
#    base-packages: com.grasswort.picker
#  registry:
#    address: nacos://192.168.150.101:8848?backup=192.168.150.102:8848,192.168.150.103:8848
#    check: false
```
* 重新启动后，调用支付 api 成功，说明外部化配置生效。