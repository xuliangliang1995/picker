# Docker 私有仓库搭建
![timg.jpg](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/d25cd1d53642f5c43a24c0cce883905e.jpg_target)

## 部署 harbor
```shell
wget https://github.com/goharbor/harbor/releases/download/v1.9.2/harbor-offline-installer-v1.9.2.tgz
tar -zxvf harbor-offline-installer-v1.9.2.tgz
cd harbor
vim harbor.yml
```
```修改 hostname 为本地 IP```
```shell
sudo ./install.sh
# 执行完后运行docker命令查看运行中的容器
docker ps 
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/3a7f35eea98493bcbd8c6b1f9f1e322a.png_target)

图中可见映射的是宿主机的 80  端口，所以在浏览器通过本机 IP 访问即可。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/654ac80fbed7b1b8ddee1b7eb61f3de0.png_target)

## 登录 harbor
admin 账户默认密码为： Harbor12345。进去后可以修改密码。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/897816d4b36af6ae21c8a46228f6cd2d.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/ebeee88d0cb03149ae7a2fe551b26a26.png_target)

## 新建项目

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/bbb4e621a54ebe9a6a23a62198217f73.png_target)

## 上传镜像
### 1. 登录
```shell
docker login -u xuliangliang 114.67.105.79:80
# windows（如果安装了 docker for windows）： winpty docker login -u xuliangliang 114.67.105.79:80
```
```docker 默认是只能 https 登录的， http 需要设置```
* linux
```shell
vim /etc/docker/daemon.json
{
  "registry-mirrors": ["http://hub-mirror.c.163.com"],
  "insecure-registries":["114.67.105.79:80"] // 主要是这个
}
sudo service docker restart // 重启 docker 
```
* windows
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/7d78582c4930819f71e27ef6b2db0974.png_target)

### 2.镜像打标签
```shell
# 如果本地没有镜像，先随便拉个 helloworld 下来演示
docker pull karthequian/helloworld 

docker tag karthequian/helloworld 114.67.105.79:80/picker/helloworld:latest
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/aa5f7137aa0f276f77e154ebd7e01aab.png_target)

```OK, 如图，我们已经打上了私有库标签```

### 3.推送到私有库
```shell
docker push 114.67.105.79:80/picker/helloworld:latest
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/a4533b05edd2023ad0bc1de199696ac1.png_target)

```OK, 推送成功，我们已经看到它了```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/4ea46959001c23c2c4953e5351f4380b.png_target)

```其它机器要使用的话，登录后使用 docker pull 拉下来即可。```
```shell
docker pull 114.67.105.79:80/picker/helloworld:latest
```