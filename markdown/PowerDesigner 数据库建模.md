# PowerDesigner 使用教程
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/96fed8c5e8c49e25bd173e617b50b76b.png_target)

该篇演示示例为：使用 PowerDesigner 创建 MySQL 数据库关系模型。以两张表为例：
* `grasswort_shop` : 商店表（下面演示的时候，填成了`grasswort_table`, 无伤大雅）
* `grasswort_shop_commodity`: 商品表

## 网盘下载

* 链接：[https://pan.baidu.com/s/1H4N-8h8WeZTyc6l9JlGslA](https://pan.baidu.com/s/1H4N-8h8WeZTyc6l9JlGslA) 
* 提取码：c5lr

## 创建表

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/1dc105a9bb2d8b2af65fb2abb9ed3dc2.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/6e1c9d6e250106a7f3167f09661ca91e.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/7eb3ae1df6f6e7a265e2fb9a8a73b398.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/5dd6c14594bd6d861c2b2d1c95d445b0.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/195eea0de146541ac85c595bf8eb3391.png_target)

添加了以下几列：
* `Default Value`
* `Comment`
* `Unsigned`
* `ZeroFull`
* `Identity`

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/a09de3b32553b8287d65d7d558c04d70.png_target)

然后添加 4 个字段

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/a24cc9a7f594468dea0c71d4f20e5360.png_target)

再创建商品表

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/457a3ab142bd805d151d03270717c7ce.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/3d5c3c0a2925f06a5ea423823800a530.png_target)

## 创建外键
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/e2d9a2cc75021a589082a57a789f56f4.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/04ea5a693ea34e9112f593f716927c1c.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/53820b1e0d07c0255fa7da874d1d1652.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/a509c91a40e772f4f90f8cf258a75c14.png_target)

## 创建索引
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/bba6825cc357b6e67344eb8dae7a7554.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/9ea8ace236967a3bcf0299c266a5f5b8.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/af03cc845fd6f0f8cc923637c859585c.png_target)

## 导出 SQL
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/711cb49a9f99cad8ad5de5655f76b2cb.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/53d3c264dcac9a93ecd58367968cd0ae.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/66918d96319835ee107193a8b2a9105f.png_target)

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200429/3e7289536dd35b3a76caf502320ed7e6.png_target)


导出文件内容如下：
```sql
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/4/29 14:26:53                           */
/*==============================================================*/


drop index idx_name on grasswort_shop_commodity;

drop table if exists grasswort_shop_commodity;

drop table if exists grasswort_table;

/*==============================================================*/
/* Table: grasswort_shop_commodity                              */
/*==============================================================*/
create table grasswort_shop_commodity
(
   id                   bigint unsigned zerofill not null,
   shop_id              bigint not null,
   name                 varchar(20) not null,
   gmt_create           datetime not null default CURRENT_TIMESTAMP,
   gmt_modified         datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (id)
);

alter table grasswort_shop_commodity comment '商店商品';

/*==============================================================*/
/* Index: idx_name                                              */
/*==============================================================*/
create index idx_name on grasswort_shop_commodity
(
   name
);

/*==============================================================*/
/* Table: grasswort_table                                       */
/*==============================================================*/
create table grasswort_table
(
   id                   bigint unsigned zerofill not null auto_increment comment 'ID',
   name                 varchar(20) not null comment '商店名称',
   gmt_create           datetime not null default CURRENT_TIMESTAMP comment '创建时间',
   gmt_modified         datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id)
);

alter table grasswort_table comment '商店';

```