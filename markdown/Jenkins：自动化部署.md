# Jenkins: 自动化部署
![微信截图_20191111220616.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/2c0d992066cd02da45caaeb538d0f6bb.png_target)
官网：[https://jenkins.io/zh/](https://jenkins.io/zh/) 

## Jenkins 安装
```shell
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo 
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
sudo yum install -y jenkins
systemctl start jenkins
```
```jenkins 默认监听 8080 端口。通过 IP:8080 进行访问。```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/4106334e6c524391ccecef78a2646d9d.png_target)

```shell
# 按照图中提示查看密码
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/4ce2781c5015b35e4c9c3535347cefcd.png_target)

```选择推荐就行。windows 安装特别慢，docker 和 linux 安装速度挺快。```

## 更换插件镜像

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/1a54bd6b4552f85e02f221c3e03ddf55.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/fc039aa693178d1ec6533cb07f885aea.png_target)

```shell
# 清华镜像地址
https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
```

## 安装常用插件

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/8ad781ba2158c97e5b6f4ba091b415cb.png_target)

```推荐安装以下本教程需要用到的插件：```

* Maven Integration 
* Docker	
* Publish Over SSH
* config file provider

```点击直接安装。勾选安装完后自动重启，等它重启```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/86660b503adf6ed75dc544d343597e6f.png_target)

## 全局工具配置

### JDK + GIT + MAVEN

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/b405b766d2964b543b84d20694082cbb.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/51e0cc30d00d6e139ae625a8af35c3fa.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/4f27a156c8bbafd9fdd0d989091cd96e.png_target)

jenkins 默认读取 /var/lib/jenkins/.m2/settings.xml 配置文件
全局默认读取 MAVEN_HOME/conf/settings.xml
如果图方便的话，直接将自己的 settings.xml 移动到 /var/lib/jenkins/.m2/ 下即可
如果各个项目不同配置的话，推荐使用 config file provider 插件。

```保存即可```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/5f6ce34de720b1ecf5d8d0d4bdd877a4.png_target)

### SSH Server

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/606bd917c794151bd83783d2df9e2f6c.png_target)

### Docker

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/b27fbc390e3d401432fadd627d62bbd1.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/40c85ecbd6d6cfb728d5104a9b2afa83.png_target)

windows 用户需要右击小海豚图像，设置监听 2375 端口
mac 用户可以执行以下命令自己监听端口：

```shell
docker run -d --restart=always -v /var/run/docker.sock:/var/run/docker.sock -p 127.0.0.1:1234:1234 bobrik/socat TCP-LISTEN:1234,fork UNIX-CONNECT:/var/run/docker.sock
```
如上， DOCKER_HOST ： tcp://localhost:1234

### maven settings.xml

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/9ac376329cbc2bb22a2978d2a2d19bb3.png_target)

## 创建任务

**任务目标**：

假设我们在 github 上有一个 maven 项目。当我们向该项目 master 分支提交代码后。jenkins 自动帮助我们：
1. 重新打包应用、上传 jar 包到 nexus 私服、并构建 docker 镜像发布到 docker 私库。
2. 如果第一步执行顺利的话，ssh 连接远程服务器执行 shell 命令更新镜像并重新部署应用。

**那么，开始咯**

1. 配置 github web hook（如果你的机器被外网访问，推荐这种。否则跳过这步）
配置 web hook 是为了，当你向 github 提交代码后，github 会通过配置 URL 通知 jenkins 开启构建。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/c6bdc0c4ccc4e327e176d73022cb29bd.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/e90fec0752c5dd7b6ebe7c712952a2d3.png_target)

* github 凭据生成

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/50911e3de05dfe898d142aa9ce1a3526.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/462e5acc8303f0a8c8f0122a3fbac617.png_target)

* github 项目下添加 webhook 地址

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/2b4f3e6be59d95652195ee5c7d7bbc6f.png_target)

2. 新建任务

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/2a5fe36cb297267979fdb1bcece8fb20.png_target)

3. 基本信息填写

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/ac56e7c4ff4831d5245b1b43a1f2db3a.png_target)

4. 源码管理

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/528cac7cab9191cfbdc04d1f53825029.png_target)

5. 构建时机

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/899e0c28f7813b829e452ed27b4ffd35.png_target)

6. 构建环境

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/65eeee1709efaec51d2d2f87d4aa95e4.png_target)

7. build

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/1a273ad57fb8c9cd19b56aaa52e20e69.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/677b7b7189f43b573d40804fe264d5bf.png_target)

**OK，到这里我们先保存一下，测试一下提交代码是否会开始自动构建。成功的话再继续第二个目标。**

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/1acc4136ccec2a774aecd57e1fa05c1a.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/634d4deb8a32379eb90a5f296c79b6b9.png_target)

```控制台输出```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/39041795d2a840197c865fc1e0086d3f.png_target)

**接下来就非常容易啦**。
```清空本地废弃镜像```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/f5c0544af5fb6e55447a0bf35c7f79d7.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/1453475a947fb9da76ac83ee6ee9bed8.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191112/0404794f224ee8eb8f57a4b0fbfb2ae0.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191113/05e08b55282dffb3e57eaf3981406f53.png_target)

**邮件配置**

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191113/2699435526c08d425091e2582b5d6ed7.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191113/a29ae7be569179c3641a340b5cdae6a8.png_target)

OK, 本教程到此结束。