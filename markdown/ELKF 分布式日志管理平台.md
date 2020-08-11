# ELKF 分布式日志集中管理平台
![微信截图_20191110194024.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191110/7729039cef346f8fae956bcc404f710f.png_target)

ELKF，即 elasticsearch, logstash, kibana, filebeat 的合称。
本教程演示的是通过这四种工具对 docker-swarm 集群下的容器进行日志搜集并集中管理。先放一张效果图。由于笔者本人（我）也是初次接触，部分知识了解并不深入。完全按照本文操作未必能满足你的需求。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/534805a7b730e53d115e0261881d10f3.png_target)

## 版本（7.4.1）

E：elasticsearch:7.4.1
L：logstash:7.4.1
K：kibana:7.4.1
F：filebeat:7.4.1

## 参考资料

由于搭建过程中资料相对缺乏，且不同版本之间存在差异。查阅资料基本来自 es 官网。需要耐心认真查看。官网链接：[https://www.elastic.co/cn/](https://www.elastic.co/cn/)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/f9031c6d2395ef1d647ed9978a03a4a3.png_target)

## 问题与思路
1. 在 docker-swarm 集群下，docker 应用是随机部署到 swarm 集群中的任意一台机器中的。也就是我们并不能确定哪个应用的日志会出现在哪台机器上。

**思路**：
```查阅资料得知 docker 应用默认日志输出格式为 json 格式。且输出文件路径为 /var/lib/docker/containers/*/*.log，所以决定在 swarm 集群中每一台机器上都部署一个 filebeat 来搜集匹配该路径（所有 docker 容器）的日志。```

即：filebeat 配置文件中会加上以下内容：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/97fea687c0c4b04b210f1cef8b3dc46e.png_target)

2. 由于我们搜集了所有 docker 容器的日志，但我们其实只想搜集特定某个容器或特定某几个容器的日志。

**思路**
```我们可以在部署 docker 应用的时候给容器打上标签。如下：```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/efa5b361fa92c5450be00c71aa8e8adf.png_target)

```然后，在 logstash 中对日志根据标签进行过滤。如下：```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/922b501341cabf4847b0e1d28ea05e59.png_target)

## 部署结构
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/9b2412dc67ec072101c8dc0ab032dc26.png_target)

## docker-compose 部署 ELK
### docker-compose.yml
```yml
version: "3"
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.4.1
    container_name: elasticsearch
    restart: always
    environment:
      - discovery.type=single-node
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms1024m -Xmx1024m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    ports:
      - 9200:9200
      - 9300:9300
    volumes:
      - "~/esdata:/usr/share/elasticsearch/data"

  kibana:
    image: docker.elastic.co/kibana/kibana:7.4.1
    container_name: kibana
    restart: always
    links:
      - elasticsearch
    depends_on:
      - elasticsearch
    ports:
      - 5601:5601
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200


  logstash:
    image: docker.elastic.co/logstash/logstash:7.4.1
    container_name: logstash
    restart: always
    links:
      - elasticsearch
    depends_on:
      - elasticsearch
    ports:
      - 5044:5044
    volumes:
      - "~/ELKF/config/logstash.conf:/usr/share/logstash/pipeline/logstash.conf"
```
### logstash.conf
```
input {
    beats {
        port => "5044"
    }
}
output {
    stdout {
        codec => rubydebug
    }
    if [json][attrs][item] == "picker" {
        elasticsearch {
            action => "index"
            index => "%{[json][attrs][servicename]}-%{[json][stream]}-%{+YYYY.MM.dd}"
            hosts => [ "elasticsearch:9200" ]
        }
    }
}
```
### 部署 filebeat
在每台服务器上部署一个 filebeat。参考文章：[https://www.elastic.co/guide/en/beats/filebeat/7.x/running-on-docker.html](https://www.elastic.co/guide/en/beats/filebeat/7.x/running-on-docker.html)
```shell
chown root /ELKF/config/filebeat.yml
docker run --name filebeat -u root -d --restart=always --privileged=true -v ~/filebeat/data/:/usr/share/filebeat/data/ -v ~/ELKF/config/filebeat.yml:/usr/share/filebeat/filebeat.yml -v /var/lib/docker/containers/:/var/lib/docker/containers/ docker.elastic.co/beats/filebeat:7.4.1
```

### filebeat.yml
```yml
filebeat.inputs:
  - type: log
    enabled: true
    paths:
      # 读取 docker 容器的日志
      - '/var/lib/docker/containers/*/*.log'
    # json 解析配置
    json.keys_under_root: false
    json.add_error_key: true
    json.overwrite_keys: true
    json.message_key: log
    # 多行配置
    multiline.pattern: '^\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}.?\d{0,3}'
    multiline.negate: true
    multiline.match: after

output.logstash:
  enabled: true
  hosts: ["IP:5044"]
```
多行配置官方文档：[https://www.elastic.co/guide/en/beats/filebeat/7.x/_examples_of_multiline_configuration.html](https://www.elastic.co/guide/en/beats/filebeat/7.x/_examples_of_multiline_configuration.html)

### 操作 kibana
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/e5ec6f588ad87fcf5ddd339bb69ca55f.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/eb0a09a19c1d9ec311c19c1247ef2ac2.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/7a59ffe4e8a715d824dced7f4d624972.png_target)