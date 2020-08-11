## Mysql 规约

### 命名

1. 是与否，必须以 is_xxx 命名。 unsigned tinyint （但是实体类要去掉 is）
2. 表名，字段名全小写。
3. 表名不适用复数名词。
4. 表名：业务名称_表的作用
5. 禁用保留字
6. 小数类型必须为 decimal.
7. 等长字符串使用 char
8. varchar 不允许超过 5000，超过 5000 定义为 TEXT ，且单独建表。
9. 表必备 三字段 id, gmt_create, gmt_modified
10. 及时更新字段注释
11. 单表行数超过 500 万行 或 2G, 才推荐分库分表
12. 合适的字符存储长度 ： 
    * tinyint unsigned  0 ~ 255
    * smallint unsigned 0 ~ 65535
    * int unsigned     0 ~ 42.9亿
    * bigint unsigned    0 ~ 10 的 19 次方

### 索引

1. 唯一特性的字段或字段组合，必须建立唯一索引

2. 超过 三个 表禁止  join (join 的字段数据类型必须绝对一致)

3. varchar 字段建立索引必须指定长度。

   1. 索引区分度： count(distinct left(列名,索引长度)) / count(*) ，保持 90% 左右

4. 严禁左模糊或者全模糊查询。（如果需要，请走搜索引擎）

5. order by 注意利用索引的有序性。

6. 利用延迟关联或者子查询优化超多分页场景。

   MySQL 并不是跳过 offset 行， 而是取 offset + N 行，然后返回放弃前 offset 行。返回 N 行。要么控制返回的总页数。要么对超过特定阈值的页数进行 SQL 改写。

   ```先定位 id ,然后再关联```

   select a.* from TABLE_1 a, (select id from TABLE_1 where 条件 limit 100000, 20) b where a.id = b.id

7. SQL 性能优化的目标，至少达到 range 级别。要求是 ref 级别。

8. 建立组合索引的时候，区分度最高的在最左边

9. pk / uk / idx



### SQL

1. count(*)

2. count(distinct col) 不计算 null, count(distinct col1, col2) 任一列为 null 都不计算。

3. 使用 ISNULL() 来判断是否为 NULL 值

4. 不得使用外键与级联

5. 禁止使用存储过程

6. 数据订正（删除，修改记录操作时）,要先 select ，避免出现误删，确认无误才能执行更新语句

7. in 操作，能避免则避免，不能避免，控制在 1000 个之内。

8. 默认使用 UTF-8，如果需要存储表情，那么使用 utf8mb4

9. xml 使用 #{}，#param#,不得使用 ${}

10. 不推荐使用 ibatis 自带的 queryForList(String statementName, int start, int size);

    其实现方式是在数据库取到所有记录，再通过 subList 取子集。

11. 不允许使用 HashMap 作为查询结果集

12. 不要写一个大而全的更新接口，不需要更新的字段不要更新。

13. @Transactional 事务不要滥用。会影响 QPS。使用事务的地方要考虑回滚。

    缓存回滚、搜索引擎回滚、消息补偿、统计修正等

    