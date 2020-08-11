# 记一次 `CentOS 7.6` 操作记录

![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200229/d7690508e62266a4ad2e2f98543c4070.jpeg_target)

该篇是之前自己第一次操作 `CentOS` 系统时做的一些记录。

## 1. 首先，创建新用户
```python
useradd -g wheel grasswort # wheel组的用户可以使用sudo命令提高执行权限
passwd grasswort
su grasswort
cd # 进入用户目录
```

## 2. 安装 `JDK`
```python
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/11.0.2+9/f51449fcd52f4d52b93a989c5c56ed3c/jdk-11.0.2_linux-x64_bin.rpm
chmod +1 jdk-11.0.2_linux-x64_bin.rpm # 为rpm包所属用户添加可执行权限
sudo rpm -ivh jdk-11.0.2_linux-x64_bin.rpm  # 执行安装命令、默认安装在/usr/java/文件夹下
java -version # 查看jdk版本（rpm安装方式不需要配置环境变量）
rm -i jdk-11.0.2_linux-x64_bin.rpm # 删除jdk安装包
```

## 3. 安装 `Tomcat`
打开 `Tomcat` 官网，复制 `tomcat tar.gz` 包的下载链接。以`tomcat9` 为例，我复制的链接为：
`http://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.16/bin/apache-tomcat-9.0.16.tar.gz`

```python
wget http://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.16/bin/apache-tomcat-9.0.16.tar.gz
tar -zxvf apache-tomcat-9.0.16.tar.gz # 解压
mv apache-tomcat-9.0.16 tomcat-grasswort # 因为我要安装多个tomcat,所以要重命名区分
```
### `Tomcat` 开启 `apr`

```python
sudo yum install apr-devel openssl-devel gcc make # 安装相关依赖
cd tomcat-grasswort/bin # 该文件夹下有个 tomcat-native的压缩包
tar -zxvf tomcat-native.tar.gz # 进行解压
cd tomcat-native-1.2.19-src/native
./configure --with-java-home=/usr/java/jdk-11.0.2/ && make  # 编译
sudo make install    #  安装日志会提示安装在 /usr/local/apr/lib下
cd # 进入主文件夹
vim .bash_profile
```

在 `.bash_profile` 文件结尾换行追加以下内容
```python
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/apr/lib
export LD_RUN_PATH=$LD_RUN_PATH:/usr/local/apr/lib
```

执行以下命令使配置生效。
```python
source bash_profile
```
* 如果需要安装多个 `tomcat`，重新解压一个`tar` 包即可，注意修改 `server.xml` 中 `port` 避免冲突即可。无需再开启 `apr`。

## 4. 安装 `maven`

最简单的方式(虽然安装的不是最新的)：
```shell
yum install maven
```
如果要安装最新的，则打开 `Maven` 官网复制 `maven tar` 包链接。以 `3.6.0` 为例：
`http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz`
```python
wget http://mirrors.hust.edu.cn/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
tar -zxvf apache-maven-3.6.0-bin.tar.gz
sudo mv apache-maven-3.6.0 /usr/local/maven
sudo vim /etc/profile # 配置maven 环境变量
```
在 `profile` 中配置 `maven` 环境变量：
```python
export MAVEN_HOME=/usr/local/maven
export PATH=$PATH:$MAVEN_HOME/bin
```
执行以下命令刷新配置，并查看 `maven` 版本。
```python
source /etc/profile
mvn -v
```

## 5. 安装 `git`

最简单的安装方式：
```python
yum install git
```

想安装最新版本，也可以按照下面操作进行：
```python
wget https://github.com/git/git/archive/v2.20.1.tar.gz  
tar -zxvf git-2.20.1.tar.gz #如果解压出错，请删了压缩包重新下载
sudo yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel gcc perl-ExtUtils-MakeMaker
cd git-2.20.1
make prefix=/usr/local/git all #编译
sudo make prefix=/usr/local/git install
sudo vim /etc/profile
```
在 `profile` 中配置 `git` 环境变量:
```python
export PATH=$PATH:/usr/local/git/bin
```
执行 `source` 命令刷新配置。
```python
source /etc/profile
git --version
```

## 6. 安装 `Redis`
```python
wget http://download.redis.io/releases/redis-5.0.3.tar.gz
tar -zxvf redis-5.0.3.tar.gz  #解压
cd redis-5.0.3/
make  #编译
make install #安装
```
`redis` 包含了以下可执行文件，如果编译后执行了 `make install` 命令，这些文件将会被复制到 `/usr/local/bin` 目录内（可以在命令行直接输入程序名称即可执行）：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200229/15a60c936053b1c9b725473bc741e7e1.png_target)

 在redis源代码目录的 `utils` 文件夹中有一个名为 `redis_init_script` 的初始化脚本文件。将初始化脚本复制到 `/etc/init.d` 目录中。

文件名为：`redis_端口号`。文件内的 `REDISPORT` 要与该端口号保持一致。

以 `6379` 为例（个人建议不要使用默认端口号，因为自己服务器曾经遭遇过两次 `redis` 入侵事件。如果使用默认端口，且允许外网访问，请一定设置密码）
```python
sudo cp redis-5.0.3/utils/redis_init_script /etc/init.d/redis_6379
```
创建如下目录：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200229/d9c84561ab6272a0787f4023f1c0aed9.png_target)

```python
sudo mkdir /etc/redis
sudo mkdir /var/redis
sudo mkdir /var/redis/6379
```

```python
#安装包下有个配置样本文件 redis.conf，复制到/etc/redis下
sudo cp redis-5.0.3/redis.conf /etc/redis/6379.conf
cd /etc/redis/
sudo vim 6379.conf
# 远程连接需要注释掉 bind 127.0.0.1
# 密码配置需要解开注释 requirepass '密码'
```
修改以下配置项：
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200229/db790017e21c15cd5acc68603c47bc8e.png_target)

```python
# 启动redis
cd /etc/init.d
./redis_6379 start
```
设置 `redis`开机自启
```python
sudo vim redis_6379
```
在 `!/bin/sh` 下方添加如下内容
```python
chkconfig: 2345 90 10 
description: Redis is a persistent key-value database
````

```python   
chkconfig redis_6379 on
# 重启
sudo reboot
# 运行如下命令查看redis状态
systemctl status redis_6379
```