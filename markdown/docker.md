# Docker 基础

## Redis 分布式锁

http://tooleek.gitee.io/lock-spring-boot-starter/



## Docker（[文档](https://www.runoob.com/docker/centos-docker-install.html)）

### 版本

* docker ce （免费版）
* docker ee （付费版） 

### 安装

```shell
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

```shell
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
# 测试运行 hello-world
sudo docker run hello-world
# 镜像加速
sudo vim /etc/docker/daemon.json
	{
      "registry-mirrors": ["http://hub-mirror.c.163.com"]
    }
```

```shell
# 删除 docker ce
$ sudo yum remove docker-ce
$ sudo rm -rf /var/lib/docker

# 配置 docker 服务
sudo usermod -aG docker xuliang
```

### 镜像

* 镜像管理

```shell
# 获取镜像
docker pull ***
# 查看镜像信息
docker images
# 添加镜像标签
docker tag ubuntu:latest myubuntu:latest
# 查看详细信息
docker inspect ubuntu:18.04
# 查看镜像历史
docker history ubuntu:18.04
# 搜寻镜像
docker search nginx
# 删除镜像
docker rmi myubuntu:latest
# 清理镜像
docker image prune (删除临时镜像)
docker image prune -a (删除所有无用镜像)
docker image prune -f (自动清理临时的遗留镜像文件层)
```

* 创建镜像

  * 基于已有容器创建

    ```shell
    docker [container] commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
    -a, --author="作者信息"
    -c, --change=[]：提交的时候执行 Dockerfile 指令，包括 CMD | ENTRYPOINT | ENV | EXPOSE | LABEL | ONBUILD | USER | VOLUME | WORKDIR 等。
    -m, --message="提交消息"
    -p, --pause=true：提交时暂停容器运行。
    # 示例：
    docker [container] commit -m "提交信息" -a "xuliangliang" a925cb403f0 test:0.1
    ```

  * 基于本地模板导入

    ```shell
    docker [container] import [OPTIONS] file | URL | -[REPOSITORY[:TAG]]
    # 要直接导入一个镜像，可以使用 OpenVZ 提供的模板来创建，或者使用其他已经导出的镜像模板来创建。 OpenVZ 模板的下载地址为：
    http://openvz.org/Download/templates/precreated。
    # 命令示例
    cat ubuntu-18.04-x86_64-minimal.tar.gz
    docker import - ubuntu:18.04
    ```

  * 基于 Dockerfile 创建（最常见的方式）

    ```shell
    docker [image] build
    ```

* 存出和载入镜像

  ```shell
  docker [image] save | load
  ```

  

  * 存出镜像

  ```shell
  docker save -o ubuntu_18.04.tar ubuntu:18.04
  # 之后，用户就可以通过复制 ubuntu_18.04.tar 文件将镜像分享给他人
  ```

  * 载入镜像

  ```shell
  docker load -i ubuntu_18.04.tar
  # 或
  docker load < ubuntu_18.04.tar
  # 这将导入镜像及其相关的元数据信息（包括标签等）
  ```

* 上传镜像

  ```shell
  docker [image] push NAME[:TAG] | [REGISTRY_HOST[:REGISTRY_PORT]/]NAME[:TAG]
  # 示例
  docker tag test:latest user/test:latest
  docker push user/test:latest
  # 第一次上传时，会提示输入登录信息或进行注册，之后登录信息会记录到本地 ~/.docker 目录下。
  ```

  

### 容器

* 创建容器

  ```shell
  docker [container] create
  # 示例
  docker create -it ubuntu:latest
  ```

* 启动容器

  ```shell
  docker [container] start
  # 示例
  docker start af
  ```

* 新建并启动容器

  ```shell
  docker [container] run
  示例
  docker run ubuntu /bin/echo 'Hello world'
  ```

* Docker 在后台运行的标准操作

  * 检查本地是否存在指定的镜像，不存在就从公有仓库下载。
  * 利用镜像创建一个容器，并启动该容器。
  * 分配一个文件系统给容器，并在只读的镜像层外挂载一层可读写层。
  * 从宿主主机配置的网桥接口中桥接一个虚拟接口到容器中去。
  * 从网桥的地址池配置一个 IP 地址给容器。
  * 执行用户指定的应用程序。
  * 执行完毕后容器被自动终止。

```shell
docker run -it ubuntu:18.04 /bin/bash
# 该命令启动一个 bash 终端，允许用户进行交互
```

```某些时候，执行 docker [container] run 时候因为命令无法正常执行容器会出错直接退出，此时可以查看退出的错误代码。```

```125：Docker daemon 执行出错，例如指定了不支持的 Docker 命令参数```

```126：所指定命令无法执行，例如权限出错。```

```127：容器内命令无法找到。```

*  守护态运行

  ```shell
  -d
  ```

* 查看容器输出

  ```shell
  docker [container] logs
  -details：打印详细信息
  -f, -follow：持续保持输出
  -since string：输出从某个时间开始的日志
  -tail string：输出最近的若干日志
  -t, -timestamps： 显示时间戳信息
  -until string：输出某个时间之前的日志
  # 示例
  docker logs ce554267d7a4
  ```

* 停止容器

  * 暂停容器

    ```shell
    docker [container] pause CONTAINER [CONTAINER...]
    # 示例：启动一个容器，并将其暂停
    docker run -name test --rm -it ubuntu bash
    docker pause test
    docker ps
    # 处于暂停状态的容器，可以通过以下命令恢复运行
    docker [container] unpause CONTAINER[CONTAINER...]
    ```

  * 终止容器

    ```shell
    docker [container] stop
    # 示例
    docker stop ce5
    # 该命令首先向容器发送 SIGTERM 信号，等待一段超时时间后（默认为 10 秒），再发送 SIGKILL 信号来终止容器。
    # 处于终止状态的容器，可以通过以下命令重新启动
    docker [container] start
    docker [container] restart # 先终止再启动
    ```

* 进入容器

  在使用 ```-d``` 参数时，容器启动后会进入后台，用户无法看到容器中的信息，也无法进行操作。这个时候如果需要进入容器进行操作，推荐使用官方的```attach``` 或 ```exec``` 命令。

  * attach

    ```shell
    docker [container] attach [--detach-keys[=[]]] [--no-stdin] [--sig-proxy[=true]] CONTAINER
    # --detach-keys[=[]]：指定退出 attach 模式的快捷键序列，默认是 CTRL-p CTRL-q.
    # --no-stdin=true | false：是否关闭标准输入，默认是保持打开
    # --sig-proxy=true | false： 是否代理收到的系统信号给应用进程，默认为 true
    ```

    ```然而，使用 attach 命令有时候并不方便。当多个窗口同时 attach 到同一个容器的时候，所有窗口都会同步显示。当某个窗口因命令阻塞时，其它窗口也无法执行操作了。```

  * exec

    ```shell
    docker [container] exec 
    	[-d | --detach] 
    	[--detach-keys[=[]]]
    	[-i | --interactive]
    	[--privileged]
    	[-t | --tty]
    	[-u | --user[=USER]]
    	CONTAINER COMMAND [ARG...]
    	
    -d, -detach：在容器中后台执行命令
    --detach-keys=""：指定将容器切回后台的按键
    -e, --env=[]：指定环境变量列表
    -i, --interactive=true | false：打开标准输入接受用户输入命令，默认值为 false
    --privileged=true | false：是否给执行命令以高权限，默认值为 false
    -t, --tty=true | false：分配伪终端，默认值为 false
    -u, --user=""：执行命令的用户名或 ID
    
    # 示例:进入容器中，并启动一个 bash
    docker exec -it 249293892slkdjf /bin/bash
    ```

* 删除容器

```shell
docker [container] rm 
	[-f | --force]
	[-l | --link]
	[-v | --volumes]
	CONTAINER [CONTAINER...]
	
-f, --force=false：是否强行终止并删除一个运行中的容器。
-l, --link=false：删除容器的连接，但保留容器
-v, --volumes=false：删除容器挂载的数据卷

# 示例
docker rm ce324234232
```

* 导出容器

```shell
docker [container] export 
	[-o | --output[=""]]
	CONTAINER
	
# 示例
docker export -o test_for_run.tar ce5
# 或
docker export e81 >test_for_stop.tar
```

* 导入容器

```shell
docker [container] import
	[-c | --change[=[]]]
	[-m | --message[=MESSAGE]]
	file | URL | -[REPOSITORY[:TAG]]
	
# 示例
docker import test_for_run.tar - test/ubuntu:v1.0
```

```实际上，也可以使用 docker load 命令来导入一个容器快照到本地镜像库。这两者的区别在于：容器快照文件将丢弃所有历史记录和元数据信息（即：仅保存容器当时的快照状态），而镜像存储文件将保存完整记录，体积更大。此外，从容器快照文件导入时可以重新指定标签等元数据信息。```

* 查看容器

```shell
docker container inspect [OPTIONS] CONTAINER[CONTAINER...]
# 示例
docker container inspect test
```

* 查看容器内进程

```shell
docker top test
```

* 查看统计信息

```shell
docker [container] stats [OPTIONS] [CONTAINER...]
# 会显示 CPU、内存、存储、网络等使用情况的统计信息。

-a, -all：输出所有容器统计信息，默认仅在运行中
-format string：格式化输出信息
-no-stream：不持续输出，默认会自动更新持续实时结果
-no-trunc：不截断输出信息
```

* 复制文件

```shell
docker [container] cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH | -

-a, -archive：打包模式，复制文件会带有原始的 uid/gid 信息。
-L, -follow-link： 跟随软连接。当原路径为软连接时，默认只复制链接信息，使用该选项会复制链接的目标内容。

# 示例
docker [container] cp data test:/tmp/
```

* 查看变更

```shell
docker [container] diff CONTAINER
# 示例
docker container diff test
```

* 查看端口映射

```shell
docker [container] [PRIVATE_PORT[/PROTO]]
# 示例
docker container port test
```

* 更新配置

```shell
docker [container] update [OPTIONS] CONTAINER [CONTAINER...]
# 示例
docker update --cpu-quota 1000000 test
```

### docker 仓库

* Docker Hub 公共镜像市场（https://hub.docker.com）

```shell
# 登录
docker login
docker search centos
```

```镜像资源分为两类```

1. 根镜像，往往使用单个单词作为名字
2. 某个用户的镜像。“user_name/镜像名”

```shell
docker pull centos
```

* 自动创建

```自动创建 是 Docker Hub 提供的自动化服务，这一功能可以自动跟随项目代码的变更而重新构建镜像。要配置自动创建，包括如下步骤：```

1. 创建并登陆 Docker Hub，以及目标网站如 Github
2. 在目标网站中允许 Docker Hub 访问服务
3. 在 Docker Hub 中配置一个“自动创建”类型的项目
4. 选取一个目标网站中的项目（需要含 Dockerfile）和分支
5. 指定 Dockerfile 的位置，并提交创建。

* 第三方镜像市场

例：时速云： https://hub.tenxcloud.com

* 搭建本地私有仓库

```shell
docker run -d -p 5000:5000 registry:2
# 这将自动下载并启动一个 registry 容器，创建本地的私有仓库服务。
# 可以通过 -v 参数来将镜像文件存放在本地的指定路径。
docker run -d -p 5000:5000 -v /opt/data/registry:/var/lib/registry registry:2
```

* 管理私有仓库

```shell
docker tag ubuntu:18.04 10.0.2.2:5000/test
docker push 10.0.2.2:5000/test
# 用 curl 命令查看仓库 10.0.2.2:5000 中的镜像：
curl http://10.0.2.2:5000/v2/search
# 比较新的 Docker 版本对安全性要求较高，会要求仓库支持 SSL/TLS 证书。
# 对于内部使用的私有仓库，可以自行配置证书或关闭对仓库的安全性检查。
# 首先，修改 Docker daemon 的启动参数，添加如下参数，表示信任这个私有仓库，不进行安全证书检查：
DOCKER_OPTS="--insecure-registry 10.0.2.2:5000"
```

### 数据卷

```数据卷（Data Volumes）是一个可供容器使用的特殊目录，它将主机操作系统目录直接映射进容器，类似于 Linux 中的 mount 行为。```

1. 数据卷可以在容器之间共享和重用，容器间传递数据将变得高效与方便
2. 对数据卷内数据的修改会立马生效，无论是容器内操作还是本地操作
3. 对数据卷的更新不会影响镜像，解耦开应用和数据。
4. 卷会一直存在，直到没有容器使用，可以安全地卸载它

* 创建数据卷

```shell
docker volume create -d local test
ls -l /var/lib/docker/volumes

inspect : 查看详细信息
ls : 列出已有数据卷
prune： 清理无用数据卷
rm： 删除数据卷
```

* 绑定数据卷

```shell
# 将主机本地的任意路径挂载到容器内作为数据卷，这种形式创建的数据卷称为绑定数据卷。在使用 docker [container] run 命令的时候，可以使用 -mount 选项类使用数据卷。
volume： 普通数据卷，映射到主机/var/lib/docker/volumes 路径下
bind： 绑定数据卷，映射到主机指定路径下
tmpfs: 临时数据卷，只存在于内存中

# 示例:创建一个 web 容器，并创建一个数据卷挂载到容器 /opt/webapp 目录：
docker run -d -p --name web --mount type=bind,source=/webapp,destination=/opt/webapp training/webapp python app.py
# 上述命令等同于使用旧的 -v 标记可以在容器内创建一个数据卷
docker run -d -p --name web -v /webapp:/opt/webapp training/webapp python app.py
# 这个功能在进行应用测试的时候十分方便，比如用户可以放置一些程序或数据到本地目录中实时进行更新，然后在容器内运行和使用。
# 另外，本地目录的路径必须是绝对路径，容器内路径可以为相对路径。如果目录不存在，Docker 会自动创建。

# Docker 挂载数据卷的默认权限是读写（rw），用户也可以通过 ro 指定为只读：
docker -run -d -p --name web -v /webapp:/opt/webapp:ro training/webapp python app.py
```

```如果直接挂载一个文件到容器，使用文件编辑工具的时候，可能会造成文件 inode的改变。推荐的方式是直接挂载文件所在的目录到容器内。```

* 数据卷容器

```如果用户需要在多个容器之间共享一些持续更新的数据，最简单的方式是使用数据卷容器。数据卷容器也是一个容器，但是它的目的是专门提供数据卷给其他容器挂载。```

```shell
# 创建一个数据卷容器 dbdata，并在其中创建一个数据卷挂载到 /dbdata
docker run -it -v /dbdata --name dbdata ubuntu
# 在其他容器中使用 --volumes-from 来挂载 dbdata 容器中的数据卷
# 例如，创建 db1、db2 两个容器，并从 dbdata 容器挂载数据卷：
docker run -it --volumes-from dbdata --name db1 ubuntu
docker run -it --volumes-from dbdata --name db2 ubuntu
# 三个容器任何一方在该目录下的写入，其他容器都可以看到
```

* 利用数据卷容器来迁移数据

1. 备份

```shell
docker run --volumes-from dbdata -v $(pwd):/backup --name worker ubuntu tar cvf /backup/backup.tar /dbdata
# 利用 ubuntu 镜像创建了一个 worker 容器，让 worker 容器挂载 dbdata 容器数据卷
# 使用 -v 参数来挂载本地当前目录到 worker 容器的 /backup 目录
# worker 启动后，使用 tar cvf /backup/backup.tar /dbdata 命令将 /dbdata 下内容备份为容器内的 /backup/backup.tar, 即宿主主机当前目录下的 backup.tar。
```

2. 恢复

```shell
docker run -v /dbdata --name dbdata2 ubuntu /bin/bash
docker run --volumes-from dbdata2 -v $(pwd):/backup busybox tar xvf /backup/backup.tar
```



### 端口映射与容器互联

* 从外部访问容器应用

```当容器中运行一些网络应用，要让外部访问这些应用时，可以通过 -P 或 -p 参数来指定端口映射。当使用 -P 标记时， Docker 会随机映射一个 49000~49900 端口到内部容器开放的网络端口```

```shell
docker run -d -P training/webapp python app.py
docker ps -l
# 0.0.0.0:49155 -> 5000/tcp
# 本地主机的 49155 被映射到了容器的 5000 端口，访问宿主主机的 49155 端口即可访问容器内的 web 应用提供的界面

# -p 则可以指定要映射的端口，并且，在一个指定端口上只可以绑定一个容器。支持的格式有 IP:HostPort:ContainerPort | IP::ContainerPort | HostPort:ContainerPort

# 示例
docker run -d -p 127.0.0.1:5000:5000 training/webapp python app.py
docker run -d -p 127.0.0.1:5000:5000/udp training/webapp python app.py

# 查看映射端口配置
docker port nostalgic_morse 5000

# 容器有自己的内部网络和IP地址，使用 docker [container] inspect + 容器ID 可以获取容器的具体信息。
```



### 互联机制实现便捷互访

1. 自定义容器命名

   1. 比较好记
   2. 当要连接其他容器时候（即便重启），也可以使用容器名而不用改变，比如连接 web 容器到 db 容器，使用 --name 标记可以为容器自定义命名。

   ```shell
   docker run -d -P --name web training/webapp python app.py
   ```

2. 容器互联

   使用 --link 参数可以让容器之间安全地进行交互。

   ```shell
   # 创建一个数据库容器
   docker run -d --name db training/postgres
   # 删除之前的 web 容器
   docker rm -f web
   # 创建一个新的 web 容器，并将它连接到 db 容器
   docker run -d -P --name web --link db:db training/webapp python app.py
   # --link 格式为 --link name:alias
   docker ps
   # Docker 相当于在两个互联的容器之间创建了一个虚拟通道，而且不用映射它们的端口到宿主主机上。在启动 db 容器的时候并没有使用 -p 和 -P 标记，从而避免了暴露数据库服务端口到外部网络上。
   ```

   ```Docker 通过两种方式为容器公开连接信息```

   ```更新环境变量```

   ```更新 /etc/hosts 文件```



### 使用 Dockerfile 创建镜像

* Dockerfile 基本结构

  * 基础镜像信息
  * 维护者信息
  * 镜像操作指令
  * 容器启动时执行指令

* Dockerfile 部分指令说明

  * ARG 创建镜像过程中使用的变量
  * FROM 基础镜像
  * LABEL 元数据标签信息
  * EXPOSE 声明镜像内服务监听的端口（仅声明，并不会自动完成端口映射）
  * ENV 环境变量
  * ENTRYPOINT 入口命令
  * VOLUME 数据卷挂载点
  * USER 指定运行容器时的用户名或UID
  * WORKDIR 为后续的RUN、CMD、ENTGRYPOINT 指令配置工作目录
  * ONBUILD 指定当基于此镜像创建子镜像时，自动执行的操作命令
  * STOPSIGNAL 接收退出的信号值
  * HEALTHCHECK 健康检查
  * SHELL 默认 shell 类型

* 创建镜像

  ```shell
  docker [image] build [OPTIONS] PATH | URL | -
  # 示例：上下文路径：/tmp/docker_builder/,希望生成的标签 builder/first_image:1.0.0
  docker build -t builder/first_iamge:1.0.0 /tmp/docker_builder/
  ```

  

  