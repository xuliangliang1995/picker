# `Windows10` 安装 `Ubuntu` 子系统
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/51313136ff53dc3edc22558af61666c0.png_target)

**参考文档** ：
* [Windows 下子系统迁移到非系统盘](https://blog.csdn.net/m0_37990055/article/details/89709963)

不过，感觉安装这玩意儿并无卵用，建议还是 [VMware 安装 CentOS8](https://grasswort.com/blog/fb516fc9a5d2a4f5.html)。

## 1. 设置开发人员选项

**`所有设置` -> `更新和安全`  -> `开发者选项` -> `开发人员模式`**

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/ddd76b225f7bd338beba6a36315347a8.png_target)

## 2. 适用于 Linux 的 Windows 子系统

**`控制面板` -> `程序` -> `启动或关闭 windows 功能` -> `适用于 Linux 的 Windows 子系统` -> `确定` -> `重启电脑`**

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/35653db4c9648b185d74454301a3783e.png_target)

## 3. 应用商店搜索 Ubuntu 并安装
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/315d278a4ed392c4bf448eb61ec4f6f8.png_target)

### 记住安装路径（仅供参考）：
请记录下安装路径名，Ubuntu 默认安装在 C 盘下，且不支持自己选择，因此我们要把它安装到别的盘下。我的安装路径是：
`C:\Users\grass\AppData\Local\Packages\CanonicalGroupLimited.Ubuntu18.04onWindows_79rhkp1fndgsc`

## 4. 转移 Ubuntu 子系统到别的盘
以转移到 `U:\Ubuntu18.04` 文件夹下为例：
1. 卸载 Ubuntu 18.04
2. 删除 C 盘安装目录文件夹（务必）
`C:\Users\grass\AppData\Local\Packages\CanonicalGroupLimited.Ubuntu18.04onWindows_79rhkp1fndgsc`
3. 以 `管理员身份`打开 `CMD` 命令行，执行以下命令：
`C:\Users\grass\AppData\Local\Packages\CanonicalGroupLimited.Ubuntu18.04onWindows_79rhkp1fndgsc` 执行该命令前，这个文件夹（或文件）不允许存在。
```shell
mklink /j C:\Users\grass\AppData\Local\Packages\CanonicalGroupLimited.Ubuntu18.04onWindows_79rhkp1fndgsc U:\Ubuntu18.04
```
4. 修改目标文件夹 `U:\Ubuntu18.04` 权限为完全控制。
5. 执行以下命令（并不知道有何用，Just Do It !）

```shell
icacls U:\Ubuntu18.04\ /grant "你的 windows 用户名:(OI)(CI)(F)
```
6. 在运行窗口中执行“services.msc”指令，在本地服务中重启 LxssManager 服务即可。（同样不知道为什么，反正执行一遍减少问题出现几率）

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/335c751d218cb484490c22ed1a0ffdc3.png_target)

7. 然后重新安装 Ubuntu 。

8. 启动过程中会提醒输入账户名称以及密码。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/6ead2a2824ca99d638771b59881ac624.png_target)


然后就可以看到文件被安装在了 U:\Ubuntu18.04下。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/c5322686b71c687d67bed815ebd0b74a.png_target)


## 目录介绍

然后，我们熟悉的 Linux 根目录是在 `LocalState\rootfs` 下。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/fa2dcb3620a1a403884393deea977603.png_target)

`mnt` 目录下，则是我们 Windows 对应的盘符：

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200426/cbb005e3cc7774e9168a1a882355c114.png_target)