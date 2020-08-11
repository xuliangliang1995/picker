# 【UML】类图
类图表示类与类之间的关系（即对象与对象之间的关系），是系统的静态视图。

![微信截图_20191218155600.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/4848176f7d4a500d1456eb4a7dff646e.png_target)

## 类的表示
#### 1. 普通类的表示
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/2cb5e77e01041f49ca31e9b971754142.png_target)
#### 2. 抽象类的表示
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/5117493c00df87cfaadd39bb0f557cbe.png_target)
#### 3. 接口的表示
两种都可以。个人偏向第二种。因为圆形在一堆矩形中比较突出。但是如果接口又实现了别的接口。圆形不能够很好地表示接口之间的关系。
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/339e8eda03947783c21d19b9176840da.png_target)

## 类与类的关系表示
#### 1. 泛化（generalization）
泛化关系是类元的一般描述和具体描述之间的关系，具体描述建立在一般描述的基础之上，并对其进行了扩展。具体描述完全拥有一般描述的特性、成员和关系， 并且包含补充的信息。泛化用从子指向父的箭头表示，指向父的是一个空三角形。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/62cca5385b5a5b5ef431e766e49f500b.png_target)

#### 2. 实现（realization）
实现是指某个类可以提供某个接口所需要的功能。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/7586b44f663c4b71b8a2debef69a039f.png_target)

#### 3. 依赖（dependency）
依赖表示一个类依赖于另一个类。即A使用了B。用带箭头的虚线纸箱被使用者。如：学生依赖于电脑。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/89d1a15c167e7db901e66404aa1c3963.png_target)

#### 4. 关联（association）
关联是一种拥有的关系，它可以是双向的，也可以是单向的。关联关系体现的是两个类，或者类与接口之间的强依赖关系，这种关系很强烈，比依赖更强，不是偶然性的，也不是临时性的，而是一种长期性，相对平等的关系。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/64a3dbcc4b9c41abd47cfbed56d89dbf.png_target)

#### 5. 聚合（Aggregation）
聚合是整体和部分的关系。部分可以离开整体而单独存在。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/b8944056bf05c8e765492cc97c5d81ac.png_target)

#### 6. 组合（Composition）
组合是整体与部分的关系。部分不可以离开整体而单独存在。例如：公司与部门。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191218/886c7eb754777b25ccd06cd3d31c95e8.png_target)

## 关系强弱顺序
泛化 = 实现 > 组合 > 聚合 > 关联 > 依赖