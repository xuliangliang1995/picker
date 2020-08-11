# Dubbo[（官方文档链接）](http://dubbo.apache.org/zh-cn/docs/user/quick-start.html)

## 服务治理的必要性

* **当服务越来越多时，服务 URL 配置管理变得非常困难，F5 硬件负载均衡器的单点压力也越来越大。** 此时需要一个服务注册中心，动态地注册和发现服务，使服务的位置透明。并通过在消费方获取服务提供方地址列表，实现软负载均衡和 Failover，降低对 F5 硬件负载均衡器的依赖，也能减少部分成本。

* **服务间依赖关系变得错踪复杂，甚至分不清哪个应用要在哪个应用之前启动，架构师都不能完整的描述应用的架构关系。** 这时，需要自动画出应用间的依赖关系图，以帮助架构师理清理关系。

* **服务的调用量越来越大，服务的容量问题就暴露出来，这个服务需要多少机器支撑？什么时候该加机器？** 为了解决这些问题，第一步，要将服务现在每天的调用量，响应时间，都统计出来，作为容量规划的参考指标。其次，要可以动态调整权重，在线上，将某台机器的权重一直加大，并在加大的过程中记录响应时间的变化，直到响应时间到达阈值，记录此时的访问量，再以此访问量乘以机器数反推总容量。

  

## 服务治理的要求

1. 注册中心  

2. 实现链路监控

3. 服务通信异常处理

4. 负载均衡  

   

## Dubbo 配置示例

```properties
dubbo:
  application:
    name: picker-user-service-provider
    owner: xuliangliang
  protocol:
    name: dubbo
    port: 20880
  # 配置文件中心
  config-center:
    address: zookeeper://172.17.130.4:2181?backup=172.17.130.3:2181,172.17.130.2:2181
  # 服务注册中心
  registry:
    address: zookeeper://172.17.130.4:2181?backup=172.17.130.3:2181,172.17.130.2:2181
    check: false
    simplified: true
    group: picker-dev
  # 元数据中心
  metadata-report:
    address: zookeeper://172.17.130.4:2181?backup=172.17.130.3:2181,172.17.130.2:2181
    retry-times: 30
    cycle-report: false
    group: picker-dev
  scan:
    base-packages: com.grasswort.picker.user.service
```



## 启动时检查（check）

```properties
dubbo.reference.com.foo.BarService.check=false
dubbo.reference.check=false
dubbo.consumer.check=false
dubbo.registry.check=false
```



## 集群容错模式（cluster）

* **Failover** 

  失败自动切换，当出现失败，重试其它服务器。通常用于“读”操作，但重试会带来更长延迟。可通过 ```retries = 2``` 来设置重试次数。

* **Failfast**

  快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。

  > 幂等性：指无论调用多少次都不会有不同结果的 HTTP 方法

* **Failsafe**

  失败安全，出现异常时直接忽略。通常用于写入审计日志等操作。

* **Failback**

  失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。

* **Forking**

  并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过```forks = 2``` 来设置最大并行数。

* **Broadcast**

  广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。



## 负载均衡（loadBalance）

* **random**（随机，按权重设置随机概率。）

  在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。

* **RoundRobin** （轮询，按公约后的权重设置轮询比率。）

  存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。

* **LeastActive**（最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。）

  使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。

* **ConsistentHash**（一致性 hash, 相同参数的请求总是发到同一提供者。）

```xml
<!-- 服务端级别配置 -->
<dubbo:service interface="..." loadbalance="roundrobin" />
<!-- 客户端级别配置 -->
<dubbo:reference interface="..." loadbalance="roundrobin" />
<!-- 服务端方法级别 -->
<dubbo:service interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>
<!-- 客户端方法级别 -->
<dubbo:reference interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:reference>
```



## 线程模型（Dispatcher + ThreadPool）

### Dispatcher

- `all` 所有消息都派发到线程池，包括请求，响应，连接事件，断开事件，心跳等。
- `direct` 所有消息都不派发到线程池，全部在 IO 线程上直接执行。
- `message` 只有请求响应消息派发到线程池，其它连接断开事件，心跳等消息，直接在 IO 线程上执行。
- `execution` 只请求消息派发到线程池，不含响应，响应和其它连接断开事件，心跳等消息，直接在 IO 线程上执行。
- `connection` 在 IO 线程上，将连接断开事件放入队列，有序逐个执行，其它消息派发到线程池。

### ThreadPool

- `fixed` 固定大小线程池，启动时建立线程，不关闭，一直持有。(缺省)
- `cached` 缓存线程池，空闲一分钟自动删除，需要时重建。
- `limited` 可伸缩线程池，但池中的线程数只会增长不会收缩。只增长不收缩的目的是为了避免收缩时突然来了大流量引起的性能问题。
- `eager` 优先创建`Worker`线程池。在任务数量大于`corePoolSize`但是小于`maximumPoolSize`时，优先创建`Worker`来处理任务。当任务数量大于`maximumPoolSize`时，将任务放入阻塞队列中。阻塞队列充满时抛出`RejectedExecutionException`。(相比于`cached`:`cached`在任务数量超过`maximumPoolSize`时直接抛出异常而不是将任务放入阻塞队列)

```xml
<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />
```



## 直连提供者（url）

```xml
<dubbo:reference id="xxxService" interface="com.alibaba.xxx.XxxService" url="dubbo://localhost:20890" />
```



## 只订阅/只注册（register/subscribe）

```xml
<dubbo:registry address="10.20.153.10:9090" register="false" />
<dubbo:registry address="10.20.153.10:9090?register=false" />
<dubbo:registry id="qdRegistry" address="10.20.141.150:9090" subscribe="false" />
<dubbo:registry id="qdRegistry" address="10.20.141.150:9090?subscribe=false" />
```



## 静态服务（dynamic）

```xml
<!-- 默认为动态服务，静态服务注册后默认为 disabled 状态，需要手动开启 -->
<dubbo:registry address="10.20.141.150:9090" dynamic="false" />
```



## 多协议

```xml
<!-- 多协议配置 -->
<dubbo:protocol name="dubbo" port="20880" />
<dubbo:protocol name="rmi" port="1099" />
<!-- 使用dubbo协议暴露服务 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" protocol="dubbo" />
<!-- 使用rmi协议暴露服务 -->
<dubbo:service interface="com.alibaba.hello.api.DemoService" version="1.0.0" ref="demoService" protocol="rmi" />
```

```xml
<!-- 多协议配置 -->
<dubbo:protocol name="dubbo" port="20880" />
<dubbo:protocol name="hessian" port="8080" />
<!-- 使用多个协议暴露服务 -->
<dubbo:service id="helloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" protocol="dubbo,hessian" />
```



## 多注册中心

```xml
<!-- 多注册中心配置 -->
<dubbo:registry id="hangzhouRegistry" address="10.20.141.150:9090" />
<dubbo:registry id="qingdaoRegistry" address="10.20.141.151:9010" default="false" />
<!-- 向多个注册中心注册 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" registry="hangzhouRegistry,qingdaoRegistry" />
</beans>
```



## 服务分组（group）[分组聚合](http://dubbo.apache.org/zh-cn/docs/user/demos/group-merger.html)

```xml
<dubbo:service group="feedback" interface="com.xxx.IndexService" />
<dubbo:service group="member" interface="com.xxx.IndexService" />
```



## 多版本（version）

当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。

可以按照以下的步骤进行版本迁移：

1. 在低压力时间段，先升级一半提供者为新版本
2. 再将所有消费者升级为新版本
3. 然后将剩下的一半提供者升级为新版本



## 参数验证（[详细](http://dubbo.apache.org/zh-cn/docs/user/demos/parameter-validation.html)）

```xml
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.0.0.GA</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>4.2.0.Final</version>
</dependency>
```



## 结果缓存（cache）

- `lru` 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。
- `threadlocal` 当前线程缓存，比如一个页面渲染，用到很多 portal，每个 portal 都要去查用户信息，通过线程缓存，可以减少这种多余访问。
- `jcache` 与 [JSR107](http://jcp.org/en/jsr/detail?id=107') 集成，可以桥接各种缓存实现。

```xml
<dubbo:reference interface="com.foo.BarService" cache="lru" />
```



## 回声测试

```xml
<dubbo:reference id="memberService" interface="com.xxx.MemberService" />
```

代码：

```java
// 远程服务引用
MemberService memberService = ctx.getBean("memberService"); 
 
EchoService echoService = (EchoService) memberService; // 强制转型为EchoService

// 回声测试可用性
String status = echoService.$echo("OK"); 
 
assert(status.equals("OK"));
```



## 上下文信息

```java
// 远程调用
xxxService.xxx();
// 本端是否为消费端，这里会返回true
boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
// 获取最后一次调用的提供方IP地址
String serverIP = RpcContext.getContext().getRemoteHost();
// 获取当前服务配置信息，所有配置信息都将转换为URL的参数
String application = RpcContext.getContext().getUrl().getParameter("application");
// 注意：每发起RPC调用，上下文状态会变化
yyyService.yyy();
```

```java
 // 本端是否为提供端，这里会返回true
boolean isProviderSide = RpcContext.getContext().isProviderSide();
// 获取调用方IP地址
String clientIP = RpcContext.getContext().getRemoteHost();
// 获取当前服务配置信息，所有配置信息都将转换为URL的参数
String application = RpcContext.getContext().getUrl().getParameter("application");
// 注意：每发起RPC调用，上下文状态会变化
yyyService.yyy();
// 此时本端变成消费端，这里会返回false
boolean isProviderSide = RpcContext.getContext().isProviderSide();
```



## 隐式传参

```java
RpcContext.getContext().setAttachment("index", "1"); // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
xxxService.xxx(); // 远程调用
// ...
```

```java
// 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
String index = RpcContext.getContext().getAttachment("index"); 
```



## 异步调用

* [Consumer 异步调用](http://dubbo.apache.org/zh-cn/docs/user/demos/async-call.html)
* [Provider 异步执行](http://dubbo.apache.org/zh-cn/docs/user/demos/async-execute-on-provider.html)



## 参数回调

```xml
<bean id="callbackService" class="com.callback.impl.CallbackServiceImpl" />
<dubbo:service interface="com.callback.CallbackService" ref="callbackService" connections="1" callbacks="1000">
    <dubbo:method name="addListener">
        <dubbo:argument index="1" callback="true" />
        <!--也可以通过指定类型的方式-->
        <!--<dubbo:argument type="com.demo.CallbackListener" callback="true" />-->
    </dubbo:method>
</dubbo:service>
```

```xml
<dubbo:reference id="callbackService" interface="com.callback.CallbackService" />
```

```java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
context.start();
CallbackService callbackService = (CallbackService) context.getBean("callbackService");
 
callbackService.addListener("foo.bar", new CallbackListener(){
    public void changed(String msg) {
        System.out.println("callback1:" + msg);
    }
});
```



## 事件通知（[官方文档](http://dubbo.apache.org/zh-cn/docs/user/demos/events-notify.html)）



## 本地存根、本地伪装（[文档](http://dubbo.apache.org/zh-cn/docs/user/demos/local-stub.html)）

容错、降级处理

注意：Stub 类和 接口类放在同一文件夹下，且 Stub 类名为 接口名+Stub 这样，客户端只需要配置 ```stub = true```

否则需要配置 ```stub = stub 类全路径```



## 延迟暴露（[文档](http://dubbo.apache.org/zh-cn/docs/user/demos/delay-publish.html)）

```prope
<!-- spring 初始化完成之后才开始暴露服务 -->
<!-- dubbo 2.6.5 之后所有服务都在 Spring 初始化完之后暴露，delay 配置的时间也在初始化之后 -->
<dubbo:service delay="-1" />
```



## 并发控制（[文档](http://dubbo.apache.org/zh-cn/docs/user/demos/concurrency-control.html)）

限制某个服务（可以具体到方法）最大占用线程数和请求数



## 延迟连接

延迟连接用于减少长连接数。当有调用发起时，再创建长连接。（只对长连接的 dubbo 协议生效）

```xml
<dubbo:protocol name="dubbo" lazy="true" />
```



## 粘滞连接

粘滞连接用于有状态服务，尽可能让客户端总是向同一提供者发起调用，除非该提供者挂了，再连另一台。

粘滞连接将自动开启[延迟连接](http://dubbo.apache.org/zh-cn/docs/user/demos/lazy-connect.html)，以减少长连接数。

```xml
<dubbo:reference id="xxxService" interface="com.xxx.XxxService" sticky="true" />
```

Dubbo 支持方法级别的粘滞连接，如果你想进行更细粒度的控制，还可以这样配置。

```xml
<dubbo:reference id="xxxService" interface="com.xxx.XxxService">
    <dubbo:mothod name="sayHello" sticky="true" />
</dubbo:reference>
```



## 令牌验证（token=true)



## 路由规则（[文档](http://dubbo.apache.org/zh-cn/docs/user/demos/routing-rule.html)）



## 配置规则（[文档](http://dubbo.apache.org/zh-cn/docs/user/demos/config-rule.html)）



## 服务降级

```java
RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://10.20.153.10:2181"));
registry.register(URL.valueOf("override://0.0.0.0/com.foo.BarService?category=configurators&dynamic=false&application=foo&mock=force:return+null"));
```



## 设置优雅停机超时时间

```properties
dubbo.service.shutdown.wait=15000
```



## 日志适配

```properties
 dubbo.application.logger=log4j
```



## 访问日志

如果你想记录每一次请求信息，可开启访问日志，类似于apache的访问日志。**注意**：此日志量比较大，请注意磁盘容量。

将访问日志输出到当前应用的log4j日志：

```xml
<dubbo:protocol accesslog="true" />
```

将访问日志输出到指定文件：

```xml
<dubbo:protocol accesslog="http://10.20.160.198/wiki/display/dubbo/foo/bar.log" />
```



## 分布式事务（暂未实现）（[跟进文档](http://dubbo.apache.org/zh-cn/docs/user/demos/distributed-transaction.html)）



## 线程栈自动 dump

当业务线程池满时，我们需要知道线程都在等待哪些资源、条件，以找到系统的瓶颈点或异常点。dubbo通过Jstack自动导出线程堆栈来保留现场，方便排查问题

默认策略:

- 导出路径，user.home标识的用户主目录
- 导出间隔，最短间隔允许每隔10分钟导出一次

指定导出路径：

```properties
# dubbo.properties
dubbo.application.dump.directory=/tmp
<dubbo:application ...>
    <dubbo:parameter key="dump.directory" value="/tmp" />
</dubbo:application>
```



## Kryo 和 FST 序列化

```xml
<dubbo:protocol name="dubbo" serialization="kryo"/>
<dubbo:protocol name="dubbo" serialization="fst"/>
```

```java
public class SerializationOptimizerImpl implements SerializationOptimizer {

    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(BidRequest.class);
        classes.add(BidResponse.class);
        classes.add(Device.class);
        classes.add(Geo.class);
        classes.add(Impression.class);
        classes.add(SeatBid.class);
        return classes;
    }
}
```

```xml
<dubbo:protocol name="dubbo" serialization="kryo" optimizer="org.apache.dubbo.demo.SerializationOptimizerImpl"/>
```

