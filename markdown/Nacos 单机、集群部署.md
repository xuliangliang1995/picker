# Nacos 单机、集群部署
![微信截图_20191110191624.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191110/6e80683a8dd42423eccd2e003cc05688.png_target)

```shell
wget https://github.com/alibaba/nacos/releases/download/1.1.3/nacos-server-1.1.3.tar.gz
tar -zxvf nacos-server-1.1.3.tar.gz
mv nacos /usr/local/nacos
```

## 单机部署
```shell
cd /usr/local/nacos/bin
sh startup.sh -m standalone
```

## 集群部署
假设我有三台服务器，分别为：
* 172.17.130.2
* 172.17.130.3
* 172.17.130.4

1. 修改```cluster.conf```配置
```shell
cd /usr/local/nacos/conf
vim cluster.conf
```
```shell
// 添加以下内容：
172.17.130.2:8848
172.17.130.3:8848
172.17.130.4:8848
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/1945f6bcfb768f1d11a98c9a077e4164.png_target)

2. 配置 mysql 数据库[(sql语句源文件)](https://github.com/alibaba/nacos/blob/master/distribution/conf/nacos-mysql.sql)

3. 修改```application.properties```

```shell
# 添加 mysql 配置
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://114.67.99.146:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=用户名
db.password= 用户密码
```

4. 集群启动
```shell
sh startup.sh
```

## Nacos 控制台
默认端口为 8848, 可以在 ```application.properties``` 中配置。
访问 ```http://IP:8848/nacos/index.html```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191127/cd16a59b427776b6c3c8cfc9db25a98e.png_target)

默认用户名：nacos
默认密码： nacos