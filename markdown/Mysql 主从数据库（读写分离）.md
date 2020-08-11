# Mysql 主从部署
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/078d25a87a417315512a312e825d8f61.png_target)
## Mysql 数据库的安装
```shell
yum list installed | grep mariadb
sudo yum -y remove mariadb* 

wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
sudo rpm -ivh mysql57-community-release-el7-11.noarch.rpm
yum repolist enabled | grep "mysql.*-community.*"
sudo yum install mysql-server

# 启动 mysql
systemctl start mysqld.service
# 停止 mysql
systemctl stop mysqld.service 
# 开机自启
systemctl enable mysqld.service 
# 查看 mysql 运行状态
systemctl status mysqld.service
```
## Mysql 登录
```shel
cat /etc/my.conf
```
```图中红线标注的是 mysqld.log 文件。里面可以查看到 root 用户临时密码。```

![微信截图_20191111124514.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/847f61bd7b0346b2f4dacd91b79c6189.png_target)

```shell
cat /var/log/mysqld.log
```
```图片红线标注的是 root 临时登录密码```

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/85677d7cadbf95090a2d2c84e0158234.png_target)

```shell
mysql -uroot -p // 登录
```
```登录成功后修改密码```
```sql
set global validate_password_policy=0;
set password=password("你的密码");     
flush privileges;
exit;
```
```shell
# 重新登录
mysql -uroot -p 
```
```数据库已经安装完毕，并登录成功。接下来会将主从数据库分开讲解。```

## 主数据库（master）
1. 创建一个新用户，并赋予所有权限。
```sql
create user 'xuliang'@'%' identified by '我的密码';
grant all privileges on *.* to 'xuliang'@'%';
```
2. 创建一个 slave 用户，从数据库会使用这个用户登录主数据库来同步数据。
```sql
create user 'slave'@'%' identified by 'slave用户密码';
GRANT REPLICATION SLAVE ON *.* to 'slave'@'%';
FLUSH PRIVILEGES;
```
3. 修改 my.cnf 
```sql
sudo vim /etc/my.cnf
```
```添加以下内容```
```shell
# 从库就是读取这个来同步数据的
log-bin=/var/lib/mysql/binlog  
# 集群节点 id， 每个节点必须保证唯一
server-id=1	
# 要同步的 schema 名称，可以写多个	       
binlog-do-db = dbName1	       
binlog-do-db = dbName2
binlog-do-db = dbName3
```
4. 修改完后要重启 mysql 服务
```shell
systemctl restart mysqld.service
```
5. 重新登录，查看 master 状态
```shell
show master status
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/b9e10023ae312b8b3e5cdda076ad1ce5.png_target)

```Binlog_Do_DB```是要同步的 schema，就是上面配置的 dbName。
```File``` 这个参数下面要用。
```Position``` 这个要先记下来。是从库同步数据的起始位置。下面要用

## 从数据库（slave）
1. 设置从库节点 server-id
```shell
sudo vim /etc/my.cnf
```
添加以下内容
```shell
# 和其它节点必须不一致
server-id=2 
```
2. 重启 mysql 数据库，并登录。设置从库只读。
```sql
systemctl restart mysqld.service
mysql -uroot -p
set global read_only=1; 
```
3. 创建一个新用户。给个 select 权限即可，这里是为了测试 read_only 是否生效，read_only 对具有 super 权限的用户不生效。所以不能给所有权限。
```sql
create user 'xuliang'@'%' identified by '我的密码';
grant select,delete,update,create,drop on *.* to 'xuliang'@'%';
```
4. 设置要同步的主库信息。（master_log_file 和 master_log_pos 取自上面的 show_master_status 内容）
```sql
change master to master_host='主库IP',master_port=3306,master_user='slave',master_password='主库slave用户的密码',master_log_file='binlog.000001',master_log_pos=154;
```
5. 开启同步
```sql
start slave;
show slave status\G;
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/c3e536b317be1ba8ccd8df6f3bc64ded.png_target)

## 测试一下是否成功
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/97c5a1fb5cfbb8706055808f158ab09b.png_target)