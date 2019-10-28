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

# 清空 tag 为 <NONE> 的镜像
```shell script
docker images|grep none|awk '{print $3 }'|xargs docker rmi
```

# 服务器重部署容器命令
```shell script
cd picker
git pull origin master
cd program
docker-compose -f docker-compose-1.yml down
cd 
docker pull registry.cn-beijing.aliyuncs.com/grasswort/email-center
docker pull registry.cn-beijing.aliyuncs.com/grasswort/oss-service-provider
docker pull registry.cn-beijing.aliyuncs.com/grasswort/blog-service-provider
docker pull registry.cn-beijing.aliyuncs.com/grasswort/user-service-provider
docker pull registry.cn-beijing.aliyuncs.com/grasswort/picker-user
docker pull registry.cn-beijing.aliyuncs.com/grasswort/picker-blog
docker pull registry.cn-beijing.aliyuncs.com/grasswort/picker-oss
docker images|grep none|awk '{print $3 }'|xargs docker rmi
docker-compose -f docker-compose-1.yml up -d
```
