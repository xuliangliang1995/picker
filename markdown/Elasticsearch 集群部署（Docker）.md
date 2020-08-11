# Elasticsearch 集群部署（Docker）
![微信截图_20191110194024.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191110/7729039cef346f8fae956bcc404f710f.png_target)

## 选择合适的版本
为什么说要选择合适的版本呢？因为我之前搭建 ELKF 安装的是当时的最新版 7.4.1。作为一个集中式日志管理平台当然是没有问题的。但是当我使用 java api 访问的时候。提示我接收到来自 6.4.3 版本的请求，但是 7.4.1 只兼容最低 6.8.0 版本的。导致无法访问。所以，选择版本与用途有关。

我的 java 项目用的依赖如下：
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
      <version>2.2.1.RELEASE</version>
    </dependency>
```
点开后，里面依赖的是 3.2.x 版本的 Spring Data Elasticsearch 

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191130/5a5863d2e0116c51ed41cb4a20d23cae.png_target)

查看版本对照，我应该安装的是 6.8.4 版本的 Elasticsearch 。

为防止版本对照更新变化，这里放上查看版本对照的 [链接](https://docs.spring.io/spring-data/elasticsearch/docs/3.2.2.RELEASE/reference/html/#new-features)。

注：不同版本部署略有差异。所以这里放上官网 6.8.5 [部署文档](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docker.html)。可以点击 other version 查看别的版本部署方式。虽有不同，但大同小异，一看便知。

## 修改 vm.max_map_count
elasticsearch 必须 修改 vm.max_map_count >= 262144
```shell
sudo vim /etc/sysctl.conf
# 添加： vm.max_map_count=262144
```

## docker-compose.yml
* IP : 127.0.0.1
```yml
version: '3.7'
services:
  elasticsearch_1:
    image: docker.elastic.co/elasticsearch/elasticsearch:6.8.4
    container_name: elasticsearch_1
    environment:
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - "discovery.zen.ping.unicast.hosts=127.0.0.1:9300,127.0.0.1:9301,127.0.0.1:9302"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata1:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
      - 9300:9300
    networks:
      - esnet

  elasticsearch_2:
    image: docker.elastic.co/elasticsearch/elasticsearch:6.8.4
    container_name: elasticsearch_2
    environment:
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - "discovery.zen.ping.unicast.hosts=127.0.0.1:9300,127.0.0.1:9301,127.0.0.1:9302"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata2:/usr/share/elasticsearch/data
    ports:
      - 9201:9200
      - 9301:9300
    networks:
      - esnet

  elasticsearch_3:
    image: docker.elastic.co/elasticsearch/elasticsearch:6.8.4
    container_name: elasticsearch_3
    environment:
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - "discovery.zen.ping.unicast.hosts=127.0.0.1:9300,127.0.0.1:9301,127.0.0.1:9302"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata3:/usr/share/elasticsearch/data
    ports:
      - 9202:9200
      - 9302:9300
    networks:
      - esnet

  kibana:
    image: docker.elastic.co/kibana/kibana:6.8.4
    container_name: kibana
    restart: always
    ports:
      - 5601:5601
    environment:
      ELASTICSEARCH_HOSTS: http://127.0.0.1:9200

volumes:
  esdata1:
    driver: local
  esdata2:
    driver: local
  esdata3:
    driver: local

networks:
  esnet:
```
以上部署了 3 个 elasticsearch 节点，和一个 kibana 管理界面。三个节点分别挂载了三个数据卷。kibana 随便指向一个节点就行。

部分参数说明：
* cluster.name
集群名称：所有节点必须保持一致，否则找不到节点
* discovery.zen.ping.unicast.hosts
这个是配置是用来发现集群其它节点的，格式如下（默认 port 9300）：IP,IP,IP
如果使用了别的端口，则：IP:PORT,IP:PORT,IP:PORT
* ES_JAVA_OPTS=-Xms512m -Xmx512m
这个是占用内存的配置，根据自己配置调整。

## 部署

```shell
docker-compose -f docker-compose.yml up -d 
```

## 查看集群健康状态
[http://127.0.0.1:9200/_cat/health?v](http://127.0.0.1:9200/_cat/health?v)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191130/8bfeca1cc7a4c9f112208d9e06c1deeb.png_target)

## kibana
[http://127.0.0.1:5601](http://127.0.0.1:5601)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191130/f461631d92c19c8406fbda343faf65ab.png_target)

## kibana Dev Tools
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191130/07821385155c85d9bdde229b8d84d89b.png_target)

## ik 分词器

**github**: [https://github.com/medcl/elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik)

**版本**: [https://github.com/medcl/elasticsearch-analysis-ik/releases](https://github.com/medcl/elasticsearch-analysis-ik/releases)

选择和自己的 elasticsearch 对应的版本：我的是 6.8.4

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191205/35cf084ab78c2261b686b1856c14ebb4.png_target)

下载下来，并解压到 ik 目录下，然后将 ik 目录挂载到容器中
```shell
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.4/elasticsearch-analysis-ik-6.8.4.zip
mkdir ik 
mv elasticsearch-analysis-ik-6.8.4.zip ik/
unzip ik/elasticsearch-analysis-ik-6.8.4.zip
```
修改 docker-compose.yml 文件，把 ik 挂载上。（三个es节点都挂载同一个目录就可以）
```yml
  elasticsearch_3:
    image: docker.elastic.co/elasticsearch/elasticsearch:6.8.4
    container_name: elasticsearch_3
    environment:
      - cluster.name=docker-cluster
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms1024m -Xmx1024m"
      - "discovery.zen.ping.unicast.hosts=114.67.105.79:9300,114.67.105.79:9301,114.67.105.79:9302"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - esdata3:/usr/share/elasticsearch/data
      # 挂载 ik 分词器
      - /home/xuliang/ik:/usr/share/elasticsearch/plugins/ik
    ports:
      - 9202:9200
      - 9302:9300
    networks:
      - esnet
```

## ik 远程词库

在 ik 解压目录下 config 文件夹下有一个文件 ```IKAnalyzer.cfg.xml``` 
```xml
<comment>IK Analyzer 扩展配置</comment>
<!--用户可以在这里配置自己的扩展字典 -->
<entry key="ext_dict"></entry>
<!--用户可以在这里配置自己的扩展停止词字典-->
<entry key="ext_stopwords"></entry>
<!--用户可以在这里配置远程扩展字典 -->
<entry key="remote_ext_dict">你的远程词库链接</entry>
<!--用户可以在这里配置远程扩展停止词字典-->
<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
```

这个远程词库链接有如下要求：
1. 响应 header 中需要包含以下字段。根据这两个字段来判断词库是否更新。（这两个字段任一个变动则说明词库已更新）
	* Last-Modified
	* ETag

2. 返回内容词语之间以换行符 \n 分隔

下面给出 java 示例：
```java
    /**
     * 接口返回规范： https://github.com/medcl/elasticsearch-analysis-ik
     * @return
     */
    @Anoymous
    @ApiOperation(value = "词库获取")
    @GetMapping
    public ResponseEntity<String> lexicon() {
	// 获取远程词库
        LexiconResponse lexiconResponse = lexiconService.lexicon();

        if (Optional.ofNullable(lexiconResponse).map(LexiconResponse::isSuccess).orElse(false)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/html;charset=utf-8"));
            headers.setLastModified(lexiconResponse.getLastModified());
	    // 我是通过 LastModified 来更新的，所以把 ETag 写死
	    // HttpHeaders 存在 setETag() 方法，但是对值格式有要求，自己又不太懂， 所以用 set 赋值
            headers.set("ETag", lexiconResponse.getETag());
            String content = lexiconResponse.getLexicon().stream().reduce((a, b) -> a + "\n" + b).orElse("");
            return new ResponseEntity<>(
                    content,
                    headers,
                    HttpStatus.OK);
        }
	// 如果获取失败，返回 304，告知词库未更新
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
```
访问词库：
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191205/a5d28424f85cd5eb73458667dd1f806d.png_target)

第二次访问浏览器检测到 ETage 和 LastModified 没有发生变化，状态码就变成了 304

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191205/44c7778056cfd40296112719f2ac00d6.png_target)

返回内容 \n 分隔
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191205/9b110c2edc59e52b3819e190dcaa6f92.png_target)

然后。把词库链接配置到上述配置文件中。重启 es 集群。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191205/2385e238d9511c3ff7ef3d6e1130b8c5.png_target)

OK，我们看到远程词库已经被加载啦。