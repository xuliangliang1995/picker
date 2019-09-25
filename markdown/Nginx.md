# Nginx 

## 安装

```shell
yum install nginx
# nginx 命令在 /usr/sbin 下
# nginx.conf 在 /etc/nginx/下
```



```shell
# 下载 nginx
wget http://nginx.org/download/nginx-1.14.0.tar.gz
# 解压
tar -zxvf nginx-1.14.0.tar.gz 
# 安装依赖
sudo yum -y install gcc gcc-c++ automake pcre pcre-devel zlib zlib-devel open openssl-devel
cd nginx-1.14.0/
# 指定安装到 /opt/soft/ngnix 的目录下
./configure --prefix=/usr/local/nginx --with-http_proxy_module --with-http_ssl_module --with-http_stub_status_module --with-http_realip_module --with-http_gzip_static_module
# 编译安装
sudo make
subo make install
# 启动
cd /usr/local/nginx-1.14.0
sbin/nginx
```



## 负载均衡策略

* 内置策略
  * 轮询
  * 加权轮询
  * IP hash
* 扩展策略
  * url hash
  * fair（类似于 dubbo 的 最少活跃）



## 进程 ID

```shell
cat /usr/local/nginx/logs/nginx.pid 
```



## Nginx 服务可接收信号

* TERM / INT  快速停止 Nginx 服务
* QUIT  平缓停止 Nginx 服务
* HUP  使用心得配置文件启动进程，之后平缓停止原有进程。（平缓重启）
* USR1 重新打开日志文件，日志切割
* USR2 平滑升级
* WINCH  平滑停止 worker process



## 启动

```
./nginx # 运行 nginx 二进制文件即可
nginx -v # 打印版本号并退出
nginx -V # 打印版本号和配置并退出
nginx -t # 测试配置正确性并退出
```



## 停止

```shell
# 发送停止信号
./nginx -g QUIT 
kill QUIT nginx.pid
kill -9 nginx.pid(不建议)
```



## 重启

```shell
nginx -g HUP 
kill HUP nginx.pid
```



## 块（作用域）

* 全局块
* events 块
  * 主要影响 Nginx 服务器与用户的网络连接，这一部分的指令对 Nginx 服务器的性能影响较大。
* http 块
  * 代理、缓存、日志定义



## 配置 Nginx 用户（组）

```shell
user user [group];
# user, 正定可以运行 Nginx 服务器的用户
# group, 可选项，指定可以运行 Nginx 服务器的用户组
```



## 配置允许生成的 worker process 数

```shell
worker_processes number | auto
```



## 配置进程 pid 存放路径

```shell
pid file;
```



## 配置错误日志存放路径

```shell
error_log file
```



## 配置文件引入

```shell
include file;
```



## 设置网络连接序列化（events)

```shell
accept mutex on | off;
```



## 是否允许同时接收多个网络连接（events)

```shell
multi_accept on | off;
```



## 事件驱动模型的选择（events)

```shell
use method;
# method 可选内容为 select、poll、kqueue、epoll、rtsig、/dev/poll 以及 eventport
```



## 配合最大连接数（events)

```shell
worker_connections number;
# 默认为 512， 不能大于操作系统支持打开的最大文件句柄数。
```



## 配置允许 sendfile 方式传输文件

```shell
sendfile on | off;
sendfile_max_chunk size;
```



## 配置连接超时间

```shell
keepalive_timeout timeout [header_timeout];
keepalive_timeout 120s 100s;
```



## 单连接请求数上限

```shell
keepalive_requests number;
```



## 配置网路监听

```shell
listen 192.168.1.10:8000
listen 192.168.1.10;
listen 8000;
```



## location 块

```shell
location [= | ~ | ~* | ^~] uri (...)
# = 严格匹配
# ~ 正则表达式，并且区分大小写
# ~ 正则表达式，不区分大小写
# 如果 URI 包含正则表示式，就必须使用 ~ | ~*
# ^~ 匹配度最高（会对uri符号编码处理）

# 配置请求根目录
root path;
# 设置网站默认首页
index index.html;
# 设置网站的错误页面
error_page code ... [=[response]] uri
error_page 404 /404.html
error_page 403 /http://a.com/forbidden.html
error_page 410 =301 /empty.gif 
# IP 黑白名单(一个命令只能设置一个ip)
allow ip;
deny ip;
# 基于密码配置访问权限
auth_basic string | off;
auth_basic_user_file file; # 用户名、密码文件
# 明文密码
name1:password1
name2:password2
# 加密
htpasswd -c -d /nginx/conf/pass_file username
```



## https 配置

```shell
    server {
     # SSL 访问端口号为 443
        listen 443;
        # 填写绑定证书的域名
        server_name www.grasswort.com;
        # 启用 SSL 功能
        ssl on;
        # 证书文件名称
        ssl_certificate /home/xuliang/ssl/1_www.grasswort.com_bundle.crt;
        # 私钥文件名称
        ssl_certificate_key /home/xuliang/ssl/2_www.grasswort.com.key;
        ssl_session_timeout 5m;
        # 请按照这个协议配置
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        # 请按照这个套件配置，配置加密套件，写法遵循 openssl 标准。
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        ssl_prefer_server_ciphers on;
        location / {
            #网站主页路径。此路径仅供参考，具体请您按照实际目录操作。
            root index.html;
            index  index.html;
        }
    }
```



```
configure arguments: 
--prefix=/usr/local/nginx 
--with-file-aio 
--with-ipv6 
--with-http_auth_request_module 
--with-http_ssl_module 
--with-http_v2_module 
--with-http_realip_module 
--with-http_addition_module 
--with-http_xslt_module=dynamic 
--with-http_image_filter_module=dynamic 
--with-http_geoip_module=dynamic 
--with-http_sub_module 
--with-http_dav_module 
--with-http_flv_module 
--with-http_mp4_module
--with-http_gunzip_module 
--with-http_gzip_static_module 
--with-http_random_index_module 
--with-http_secure_link_module 
--with-http_degradation_module 
--with-http_slice_module 
--with-http_stub_status_module 
--with-http_perl_module=dynamic 
--with-mail=dynamic 
--with-mail_ssl_module 
--with-pcre 
--with-pcre-jit 
--with-stream=dynamic 
--with-stream_ssl_module 
--with-google_perftools_module 

```

