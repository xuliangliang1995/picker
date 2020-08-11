# Nexus：Maven私服搭建
![微信截图_20191111191330.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/16089db0297ea034389461756338b749.png_target)

## Nexus 安装
```shell
# 国内下载超级慢 ！！！所以自己上传到了阿里云上。
wget https://nexus-bucket.oss-cn-beijing.aliyuncs.com/nexus-3.18.1-01-unix.tar.gz
tar -zxvf nexus-3.18.1-01-unix.tar.gz

# 解压后会有两个目录，一个是 nexus-3.18.1-01，另一个是 sonatype-work
# 如果要移动目录的话，请把两个都移动了。否则相对路径会改变，就需要修改配置文件nexus.vmoptions
cd nexus-3.18.1-01/bin/

# 前台启动
sh nexus run 
# 后台启动
sh nexus start
# 终止命令
sh nexus stop
```

```
默认监听 8081 端口，启动后稍等片刻可以通过 http://ip:8081 进行访问。
如果访问不了，则去 soantype-work 包中去找 log 文件查看。
默认初始密码存储地址：sonatype-work/nexus3/admin.password，用户名：admin
```
## 登录 Nexus
```shell
# 查看 admin 用户密码
cat sonatype-work/nexus3/admin.password 
```
## 查看仓库
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/f66eceafde6d4a98d5c82aae38a58897.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/9f65cf3ae65e081e5f59b4cd5bede7ac.png_target)
## 创建角色
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/2cc74360dcae1e25eef0fb2810e13905.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/8a5c1a7bcb4760f1baaed7de096f7156.png_target)

## 创建用户
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/8a7f43060d56d3d0b9726bba96d69d79.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/cbafedb37b807b47ea6720dd34a5f3fc.png_target)

## settings.xml
以下是自己从网上找的配置文件参考，也许会有累赘，但可以用。有时间再细究。
* settings.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <pluginGroups></pluginGroups>
  <proxies></proxies>

  <servers>
    <server>
	<id>nexus-xuliangliang</id>
	<username>xuliangliang</username>
	<password>xuliangliang0422</password>
    </server>
    
    <server>
      <id>releases</id>
      <username>xuliangliang</username>
      <password>xuliangliang0422</password>
    </server>
    
    <server>
       <id>snapshots</id>
       <username>xuliangliang</username>
       <password>xuliangliang0422</password>
     </server>
  </servers>
   
  <mirrors>
    <mirror>
      <id>nexus-xuliangliang</id>
      <name>nexus-xuliangliang</name>
      <url>http://59.110.163.6:8081/repository/maven-public/</url>
      <mirrorOf>*</mirrorOf>        
    </mirror>
  </mirrors>

  <profiles>
  
    <profile>
       <id>jdk-1.8</id>
      <activation>
         <activeByDefault>true</activeByDefault>
         <jdk>1.8</jdk>
      </activation>
      <properties>
         <maven.compiler.source>1.8</maven.compiler.source>
         <maven.compiler.target>1.8</maven.compiler.target>
         <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
    </profile>

    <profile>
      <id>nexus-maven</id>
      <!-- 远程仓库列表 -->
      <repositories>
        <repository>
          <id>nexus-xuliangliang</id>
          <name>nexus-xuliangliang</name>
          <!-- 虚拟的URL形式,指向镜像的URL-->
          <url>http://59.110.163.6:8081/repository/maven-public/</url>
          <layout>default</layout>
          <!-- 表示可以从这个仓库下载releases版本的构件-->  
          <releases>
            <enabled>true</enabled>
          </releases>
          <!-- 表示可以从这个仓库下载snapshot版本的构件 -->  
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
      <!-- 插件仓库列表 -->
      <pluginRepositories>
        <pluginRepository>
          <id>nexus-xuliangliang</id>
          <name>nexus-xuliangliang</name>
          <url>http://59.110.163.6:8081/repository/maven-public/</url>
          <layout>default</layout>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
          <releases>
            <enabled>true</enabled>
          </releases>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  
  </profiles>
 
  <activeProfiles>
    <activeProfile>jdk-1.8</activeProfile>
    <activeProfile>nexus-maven</activeProfile>
  </activeProfiles>
</settings>
```
## pom.xml
```xml
<distributionManagement>
    <repository>
      <id>releases</id>
      <url>http://59.110.163.6:8081/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
      <id>snapshots</id>
      <url>http://59.110.163.6:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```
## 测试
```shell
mvn clean install deploy
```
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/68eea9cd360eeb39d2d27aa82f9f8ae4.png_target)

## 添加定时删除快照任务

运行了一段时间后，发现磁盘满啦,导致 nexus 上传不了。查看是因为快照版本保存的太多了。导致这个文件夹```sonatype-work/nexus3/blobs/default/content/```很大。不建议直接删除，因为我看不懂。所以添加了定时清理磁盘任务：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191118/2041291e3e346216658ad940c5d8fff5.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191118/f5f61bd8919435da60049984d27ea1d4.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191118/364b366e4231bcaa3496437aecf0dd8a.png_target)

任务也可以手动触发，点击 run 按钮即可。然后稍等片刻，就可以看到磁盘慢慢地一点一点释放出来。