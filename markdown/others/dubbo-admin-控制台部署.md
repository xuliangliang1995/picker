# dubbo-admin 控制台部署参考教程

### 一、官方教程链接：[GO](http://dubbo.apache.org/zh-cn/docs/admin/introduction.html)

### Maven方式部署

```sh
git clone https://github.com/apache/dubbo-admin.git
cd dubbo-admin
mvn clean package
cd dubbo-admin-distribution/target
java -jar dubbo-admin-0.1.jar
```

### 前后端分离部署

- 前端

```sh
cd dubbo-admin-ui 
npm install 
npm run dev 
```

- 后端

```sh
cd dubbo-admin-server
mvn clean package 
cd target
java -jar dubbo-admin-server-0.1.jar
```

### 二、配置对应的 zookeeper 集群

1. 配置文件：```dubbo-admin-server 项目下的 application.properties```

2. 自己修改后的配置：

   ```
   # centers in dubbo2.7
   admin.registry.address=zookeeper://182.92.3.187:2181?backup=182.92.160.62:2181,39.96.42.239:2181
   
   admin.config-center=zookeeper://182.92.3.187:2181?backup=182.92.160.62:2181,39.96.42.239:2181
   
   admin.metadata-report.address=zookeeper://182.92.3.187:2181?backup=182.92.160.62:2181,39.96.42.239:2181
   
   #group
   admin.registry.group=picker-dev
   admin.config-center.group=picker-dev
   admin.metadata-report.group=picker-dev
   ```

   

### 三、可能遇到的坑

1. 提示 ```npm run build``` 失败的问题，这是在对 ```dubbo-admin-ui``` 前端项目进行 ```build``` 生成 ```html```等静态页面文件时产生的错误。这里结合网上资料列举三个可能导致该问题的原因。

   > 如果看不出什么原因导致的，可以尝试把以下所有命令都执行一遍。

   * 服务器内存不足

     * 解决方法：创建交换空间

       ```shell
       # 查看是否已经存在交换空间
       swapon -s 
       cd /
       sudo fallocate -l 2G /swapfile
       sudo chmod 600 /swapfile
       sudo mkswap /swapfile
       sudo swapon /swapfile
       swapon -s
       free -m
       
       # 以下命令会使交换空间永久生效
       sudo vim /etc/fstab
       	# 换行追加以下内容
       	/swapfile   swap    swap    sw  0   0
       ```

       

   * 执行 ```npm install``` 时报错（这个个人理解就类似我们后端的 ```maven``` 吧）

     * 解决方法：更新下 ```openssl```

        ```shell
       sudo yum update openssl -y
        ```

     

   * ```TestMethod.vue``` 文件中多了一个逗号

     * 解决方法：删除逗号。文件地址：

       ```shell
       dubbo-admin/dubbo-admin-ui/src/components/test/TestMethod.vue
       ```

       在 ```dubbo-admin-ui``` 项目下执行 ```npm run build``` 会报错，然后提示你在第几行，我执行的时候在 61 行，删除即可。

   

2. 执行``` mvn clean packae``` 的时候，如果单元测试没有通过，它的项目单元测试好像有时候确实会出现问题，加上```-Dmaven.test.skip=true``` 跳过。

   ```shell
   mvn clean packae -Dmaven.test.skip=true
   ```

   

