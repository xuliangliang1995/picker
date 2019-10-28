# 常用命令记录
```shell script
# 重新打包并发布到 nenux 私服，并构建 docker 镜像推送到 docker 私服
mvn clean package deploy dockerfile:build -DpushImage
```
* docker-swarm.yml 为 docker swarm 集群部署容器使用文件
```shell script
docker stack deploy -c docker-compose.yml picker
```

* docker-compose-*.yml 是使用 docker-compose 命令部署的文件（由于自己购买的服务器内网不通，故采用此策略部署）
```shell script
docker-compose -f docker-compose-1.yml up -d 
docker-compose -f docker-compose-2.yml up -d 
docker-compose -f docker-compose-3.yml up -d 
```