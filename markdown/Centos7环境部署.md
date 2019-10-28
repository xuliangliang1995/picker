**以下为阿里云 ECS 服务器（Centos 7）操作记录**

1. 创建新用户

   ```shell
   # wheel 组是 Centos 默认存在的用户组，该用户组可以使用 sudo 命令来提高自己的权限
   useradd -g wheel xuliang
   passwd xuliang
   su xuliang
   cd 
   ```
   
2. 安装 JDK

   ```shell
   # 先从 ORACLE 官网下载 rpm 包, rpm 安装方式默认安装在 /usr/java 路径下，不需要配置 JAVA_HOME
   # 这里记录一下 4（r）代表“读”的权限， 2（w）代表“写”的权限， 1（x）代表“执行”的权限。
   # +1 就代表给所属者添加“执行”权限，+1 也可以改为 +x。
   # +6 因为 6 = 4 + 2 ，表示的就是添加“读”+“写”的权限。
   # +666 依次代表给 所属者、所属组、其它用户 添加 6 的权限。
   chmod +1 jdk-11.0.2_linux-x64_bin.rpm 
   sudo rpm -ivh jdk-11.0.2_linux-x64_bin.rpm 
   ```

3. 安装 maven

   ```shell
   wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz
   tar -zxvf apache-maven-3.6.2-bin.tar.gz
   sudo mv apache-maven-3.6.2 /usr/local/maven
   sudo vim /etc/profile 
   		# 配置maven 环境变量
           export MAVEN_HOME=/usr/local/maven
           export PATH=$PATH:$MAVEN_HOME/bin
   source /etc/profile
   mvn -v
   ```

4. 安装 git

   ```shell
   yum install git
   ```

5. Mysql 主备部署（[参考文档](https://www.cnblogs.com/lenve/p/10855172.html)）（[高可用方案](https://www.cnblogs.com/robbinluobo/p/8294782.html)）

   ```shell
   yum list installed | grep mariadb
   sudo yum -y remove mariadb* 
   wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
   sudo rpm -ivh mysql57-community-release-el7-11.noarch.rpm
   yum repolist enabled | grep "mysql.*-community.*"
   sudo yum install mysql-server
   // 启动 mysql
   systemctl start mysqld.service
   // 停止 mysql
   systemctl stop mysqld.service 
   // 开机自启
   systemctl enable mysqld.service 
   ```

   ```shell
   // 登录 mysql,有的版本需要密码，从mysql日志文件中找
   mysql -uroot -p 
   set global validate_password_policy=0;
   set password=password("xol4l2y2xx");     
   flush privileges;
   
   // master
   create user 'xuliang'@'%' identified by 'xol4l2y2xx';
   grant all privileges on *.* to 'xuliang'@'%' ;
   create user 'slave'@'%' identified by 'Xol4l2y2xx@';
   GRANT REPLICATION SLAVE ON *.* to 'slave'@'%' identified by 'Xol4l2y2xx@';
   FLUSH PRIVILEGES;
   show master status;
   
   // slave
   set global read_only=1;  
   create user 'xuliang'@'%' identified by 'xol4l2y2xx';
   # 这里给个 select 权限即可，这里是为了测试 read_only 是否生效，read_only 对具有 super 权限的用户不生效
   grant select,delete,update,create,drop on *.* to 'xuliang'@'%';
   change master to master_host='172.17.130.5',master_port=3306,master_user='slave',master_password='Xol4l2y2xx@',master_log_file='binlog.000001',master_log_pos=154;
   start slave;
   show slave status\G;
   ```

6. Nacos 集群部署

   ```shell
   wget https://github.com/alibaba/nacos/releases/download/1.1.3/nacos-server-1.1.3.tar.gz
   tar -zxvf nacos-server-1.1.3.tar.gz
   mv nacos /usr/local/nacos
   cd /usr/local/nacos/bin
   # 单机模式运行
   sh startup.sh -m standalone
   
   // 集群
   172.17.130.2:8848
   172.17.130.3:8848
   172.17.130.4:8848
   // 数据库
   spring.datasource.platform=mysql
   db.num=1
   db.url.0=jdbc:mysql://172.17.130.5:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
   db.user=xuliang
   db.password=xol4l2y2xx
   ```

7. 创建交换空间

   ```shell
   swapon -s
   cd /
   sudo fallocate -l 2G /swapfile
   sudo chmod 600 /swapfile
   sudo mkswap /swapfile
   sudo swapon /swapfile
   swapon -s
   free -m
   sudo vim /etc/fstab
   	/swapfile   swap    swap    sw  0   0
   // 修改 vm.swappiness（0 -> 30）
   cat /proc/sys/vm/swappiness
   sudo sysctl vm.swappiness=30
   
   // 永久生效
   sudo vim /etc/sysctl.conf
   	vm.swappiness = 30
   	
   // 修改 vfs_cache_pressure（100 -> 50）
   cat /proc/sys/vm/vfs_cache_pressure
   sudo sysctl vm.vfs_cache_pressure=50
   // 永久生效
   sudo vim /etc/sysctl.conf
   	vm.vfs_cache_pressure = 50
   ```


8. Zookeeper 集群部署

   ```shell
   wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/stable/apache-zookeeper-3.5.5-bin.tar.gz
   tar -zxvf apache-zookeeper-3.5.5-bin
   sudo mv apache-zookeeper-3.5.5-bin /usr/local/zookeeper
   cd /usr/local/zookeeper/conf
   cp zoo_sample.cfg zoo.cfg
   vim zoo.cfg
   	server.1=172.17.130.2:2888:3888
   	server.2=172.17.130.3:2888:3888
   	server.3=172.17.130.4:2888:3888
   	
   cd /tmp/zookeeper/
   touch myid
   vim myid
   	1 // 对应 server.1 的 1
   ```


9. 修改主机名

   ```shell
   hostnamectl set-hostname ***
   ```

10. 文件传输

    ```shell
    1）scp  本地linux系统文件路径   远程用户名@IP地址：远程系统文件路径名
    2）scp  远程用户名@IP地址：远程linux系统文件绝对路径 本地系统文件路径
    ```

11. 个人博客记录的别的一些配置：https://mp.csdn.net/postedit/88598647

12.  docker redis

    ```shell
    docker run -d -p 6379:6379 -v ~/redisdata:/data --restart=always --name redis redis:latest redis-server --requirepass "Xol4l2y2xx" --appendonly yes
    ```

    

## 部分配置信息：

### zookeeper-cluster

- 内网：```172.17.130.4:2181,172.17.130.3:2181,172.17.130.2:2181```
- 公网：```182.92.3.187:2181,182.92.160.62:2181,39.96.42.239:2181```
