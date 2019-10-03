# Kafka集群搭建

```shell
wget http://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz
tar -zxvf kafka_2.12-2.3.0.tgz
sudo mv kafka_2.12-2.3.0 /usr/local/kafka
cd /usr/local/kafka/config
vim server.properties
	listeners=PLAINTEXT://172.17.130.3:9092 
	advertised.listeners=PLAINTEXT://182.92.160.62:9092 # 如果需要外网访问，配置
	broker.id=?
	zookeeper.connect=zookeeper.grasswort.com:2181
cd /usr/local/kafka/bin
./kafka-server-start.sh -daemon ../config/server.properties &
```

