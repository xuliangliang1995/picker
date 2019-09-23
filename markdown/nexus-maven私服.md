# Nexus - Maven 私服部署参考教程

1. 安装 Nexus , 这里以 Centos 7 为例。

   国内下载 Nexus 非常慢，这里提供了自己的：[Nexus下载指引](https://github.com/xuliangliang1995/nexus)

   ```shell
   # 下载 Nexus（这个链接是我自己的，下载很快，可以直接使用）
   wget https://nexus-bucket.oss-cn-beijing.aliyuncs.com/nexus-3.18.1-01-unix.tar.gz
   # 解压
   tar -zxvf nexus-3.18.1-01-unix.tar.gz
   # 解压后会有两个目录，一个是 nexus-3.18.1-01，另一个是sonatype-work
   cd nexus-3.18.1-01/bin/
   
   # 注意有一个文件 nexus.vmoptions，里面信息如下，如果移动包路径注意修改这里面的配置
   	-Xms2703m
       -Xmx2703m
       -XX:MaxDirectMemorySize=2703m
       -XX:+UnlockDiagnosticVMOptions
       -XX:+UnsyncloadClass
       -XX:+LogVMOutput
       -XX:LogFile=../sonatype-work/nexus3/log/jvm.log
       -XX:-OmitStackTraceInFastThrow
       -Djava.net.preferIPv4Stack=true
       -Dkaraf.home=.
       -Dkaraf.base=.
       -Dkaraf.etc=etc/karaf
       -Djava.util.logging.config.file=etc/karaf/java.util.logging.properties
       -Dkaraf.data=../sonatype-work/nexus3
       -Djava.io.tmpdir=../sonatype-work/nexus3/tmp
       -Dkaraf.startLocalConsole=false
       
   # 前台启动
   sh nexus run 
   # 后台启动
   sh nexus start
   # 终止命令
   sh nexus stop
   
   # 默认监听 8081 端口，启动后稍等片刻可以通过 http://ip:8081 进行访问
   # 如果访问不了，则去 soantype-work 包中去找 log 文件查看
   # 默认初始密码存储地址：sonatype-work/nexus3/admin.password，用户名：admin
   ```

2. 配置仓库（[参考文档](https://blog.csdn.net/qq_28666081/article/details/83270713)）

3. Maven settings.xml 配置（[参考文档](https://www.cnblogs.com/qdhxhz/p/9808642.html)）

4. 放一份自己的配置文件留做以后参考

   ```java
   <?xml version="1.0" encoding="UTF-8"?>
   
   <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
     <!-- localRepository
      | The path to the local repository maven will use to store artifacts.
      |
      | Default: ${user.home}/.m2/repository
     -->
   
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

5. ```pom.xml```

   ```java
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

   