## Royal Ts：mac Xshell 替代工具

![QQ20191226095728.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/3c6feb4a4858778139a3e36c46efa6fa.png_target)

## Royal Ts 下载
官网：[https://www.royalapps.com/ts/win/download](https://www.royalapps.com/ts/win/download)

## 插件安装
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191225/2eea3ead44c647766c02031e50ac10e0.png_target)
* Terminal（ssh 连接）
* File Transfer (SFTP 文件传输)

## New Document

注：免费版只能创建一个 Document，但我们可以在一个 Document 里面创建多个连接（或者文件夹），因此，并不影响我们使用。

![QQ20191226093232.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/c4138e273a119fa907af93a272dd9a6c.png_target)

**创建完之后记得 Command + S 保存。**

## SSH 连接

![QQ20191226091814.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/2168cfd5e7bd29505b296920c30ea4cd.png_target)

![QQ20191226092705.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/aa9a4050425c5785be715e862964e9a9.png_target)

![QQ20191226092950.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/31b2daebb777ade0e7f1d7bbdac8078b.png_target)

apply -> command + S 保存。

![QQ20191226094012.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/bae8c8aeb44c87f9c0d806f840408853.png_target)

注：这里可能会出现中文乱码问题。但是用 mac 自带终端远程连接却是正常的。百度得知，执行如下操作然后重启解决：

```shell
vim /etc/ssh/ssh_config
# 添加如下内容
SendEnv LANG LC_ALL=en.US.UTF-8
```

## FTP 文件传输

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/cea5cb64b8c03d2eecca620e193032f9.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/e7133671109d77e93b11bfc3bd1db400.png_target)

添加登陆凭据（同上）

apply -> command + s 保存

双击打开效果如图：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191226/8843be2fc1429adb77ff0cf78d7bb9f6.png_target)

左右拖拽进行上传和下载操作。