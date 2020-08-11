# Zookeepr-Cluster-Docker

## 采用第三方digitalwonderland/zookeeper

### 单机

```shell
$ docker run -d -p 2181:2181 -p 2888:2888 -p 3888:3888 digitalwonderland/zookeeper
```

### 集群

* 172.17.130.2

```shell
docker run -d -p 2181:2181 -p 2888:2888 -p 3888:3888 -e ZOOKEEPER_ID=1 -e ZOOKEEPER_SERVER_1=172.17.130.2 -e ZOOKEEPER_SERVER_2=172.17.130.3 -e ZOOKEEPER_SERVER_3=172.17.130.4 digitalwonderland/zookeeper
```

* 172.17.130.3

```shell
docker run -d -p 2181:2181 -p 2888:2888 -p 3888:3888 -e ZOOKEEPER_ID=2 -e ZOOKEEPER_SERVER_1=172.17.130.2 -e ZOOKEEPER_SERVER_2=172.17.130.3 -e ZOOKEEPER_SERVER_3=172.17.130.4 digitalwonderland/zookeeper
```

* 172.17.130.4

```shell
docker run -d -p 2181:2181 -p 2888:2888 -p 3888:3888 -e ZOOKEEPER_ID=3 -e ZOOKEEPER_SERVER_1=172.17.130.2 -e ZOOKEEPER_SERVER_2=172.17.130.3 -e ZOOKEEPER_SERVER_3=172.17.130.4 digitalwonderland/zookeeper
```



## docker-compose(下面的没有跑起来,网路问题，没有找出原因)

* 172.17.130.2

```yml
version: '2'
services:
    zoo:
        image: zookeeper
        restart: always
        container_name: zookeeper_1
        ports:
            - "2181:2181"
        environment:
            ZOO_MY_ID: 1
            ZOO_SERVERS: server.1=172.17.130.2:2888:3888 server.2=172.17.130.3:2888:3888 server.3=172.17.130.4:2888:3888
```

* 172.17.130.3

```shell
version: '2'
services:
    zoo:
        image: zookeeper
        restart: always
        container_name: zookeeper_2
        ports:
            - "2181:2181"
        environment:
            ZOO_MY_ID: 2
            ZOO_SERVERS: server.1=172.17.130.2:2888:3888 server.2=172.17.130.3:2888:3888 server.3=172.17.130.4:2888:3888
```

* 172.17.130.4

```shell
version: '2'
services:
    zoo:
        image: zookeeper
        restart: always
        container_name: zookeeper_3
        ports:
            - "2181:2181"
        environment:
            ZOO_MY_ID: 3
            ZOO_SERVERS: server.1=172.17.130.2:2888:3888 server.2=172.17.130.3:2888:3888 server.3=172.17.130.4:2888:3888
```

```shell
docker-compose -f docker-compose-zookeeper_*.yml up -d
```

