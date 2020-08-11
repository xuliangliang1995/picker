# `Centos` 安装 `Docker`
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200320/18a7165a6ccc839e79b0b5b8b4c75218.jpg_target)

`Docker` 有 `docker ce` 和 `docker ee` 两个版本。
`docker ce` 为免费版。

# 安装 `docker ce`

1. 移除旧版 `docker`
```python
# 移除旧版 docker
yum remove docker \
           docker-client \
           docker-client-latest \
           docker-common \
           docker-latest \
           docker-latest-logrotate \
           docker-logrotate \
           docker-selinux \
           docker-engine-selinux \
           docker-engine
```

2. 安装 `docker ce`
```python
# yum 更新
sudo yum update
# 安装必要系统工具
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
# 配置镜像源
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# 更新 yum 缓存
sudo yum makecache fast
# 安装 docker-ce
sudo yum -y install docker-ce
# 启动 docker 后台服务
sudo systemctl start docker
# 测试运行 hello-world (可跳过)
sudo docker run hello-world
```

3. 配置镜像加速（国内镜像）
```python
sudo vim /etc/docker/daemon.json
```
在 `daemon.json` 中添加以下内容（文件不存在，可以自行创建）：
```json
{
    "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
```

4. 将当前用户（`grasswort`）加入 `docker` 组
```python
sudo usermod -aG docker grasswort
# 重启 docker 服务
systemctl daemon-reload
systemctl restart docker.service
```
切换到别的用户再切换回来，使配置生效。

5. 查看 `docker` 信息
```shell
docker info
```

## 安装 `docker compose` 编排工具。

打开以下链接：[https://github.com/docker/compose/releases](https://github.com/docker/compose/releases)
复制最新版的 `run.sh` 下载链接：
```python
https://github.com/docker/compose/releases/download/1.25.4/run.sh
```
安装 `docker compose`
```python
curl -L https://github.com/docker/compose/releases/download/1.25.4/run.sh > /usr/local/bin/docker-compose
chmod +1 /usr/local/bin/docker-compose
docker-compose --version
```

## docker-machine

### 安装

```shell
wget https://github.com/docker/machine/releases/download/v0.13.0/docker-machine-Linux-x86_64

chmod +1 docker-machine-Linux-x86_64

sudo cp docker-machine-Linux-x86_64 /usr/local/bin/docker-machine

docker-machine -v
```

## 免密登录远程服务器

```shell
ssh-keygen -t rsa # 生成密钥对
ssh-copy-id root@123.57.238.217 # 上传到远程服务器
# 之后就可以通过 ssh root@123.57.238.217 直接登录啦、但是这里不登录
```

## 远程构建 docker 环境

```shell
# 所以采用下面的命令创建了 3 个 docker-machine
docker-machine create -d generic --generic-ip-address=123.57.238.217 -generic-ssh-user=root machine-1

docker-machine create -d generic --generic-ip-address=101.200.149.59 -generic-ssh-user=root machine-2

docker-machine create -d generic --generic-ip-address=123.57.238.36 -generic-ssh-user=root machine-3

docker-machine create -d generic --generic-ip-address=114.67.99.146 -generic-ssh-user=root machine-4
```

### 将用户加入 docker 组

```shell
sudo usermod -aG docker xuliang
systemctl daemon-reload
systemctl restart docker.service
# 切换到别的用户再切换回来，使配置生效
```

## docker swarm

### 创建集群

```shell
docker swarm init --advertise-addr 114.67.84.153
```

```shell
# 执行成功后内容：l0dqhmz0k4cnzgko00bl75aoz 为集群唯一 id
Swarm initialized: current node (l0dqhmz0k4cnzgko00bl75aoz) is now a manager.
To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-41506yinl030jie2c2mw5a1p3j5i28vxh01kgwh3uwiekyam50-7zwp0tlh853bizlzgeia6eq3f 114.67.84.153:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
```

```shell
# 两个查询命令
docker info
docker node ls
```

```shell
# 拉一个自己在阿里云存储的镜像
docker pull registry.cn-beijing.aliyuncs.com/grasswort/picker-user:latest

# 创建镜像服务（副本数为：2）
docker service create --replicas 2 --name picker-user --publish published=10001,target=10001 registry.cn-beijing.aliyuncs.com/grasswort/picker-user:latest

# 扩展服务
docker service scale picker-user=3
```



```shell
# 通过 stack 命令
docker stack deploy -c docker-compose.yml picker-stack
docker stack ls
docker service ls
```