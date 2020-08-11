# Quartz  任务调度
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191207/7a4c9b4468e09e10261dad330a822f82.png_target)
## 官网
[http://www.quartz-scheduler.org](http://www.quartz-scheduler.org)
## 为什么我们需要任务调度中心？
因为我们的业务中往往包含一些需要按照一定规则周期性执行的任务。比如：每天的数据汇总、每月的财务报表等。这些任务呢，如果我们一个一个去管理的话，会非常的复杂和混乱。所以我们需要一个“中心”，能够帮我们管理这些任务，并对这些任务进行调度。

## 任务调度中心有什么要求呢？
或者说，我们想要的任务调度中心是怎么样的呢？

首先，它应该具备 “调度” 的能力。它能够帮我们按照一定的时间规则去触发任务的执行。这句话中包含了三个对象。“它”，“规则”，“任务”。所以任务调度中心的基本功能要求包括：
1. 定义任务
2. 配置任务执行规则
3. 调度器

然后呢，任务调度中心，它还要体现出它的“中心”二字。我们不希望这些任务、规则乱七八糟地分布在项目各个位置。我们希望可以对任务以及规则进行集中配置和管理。

4. 集中配置

考虑到实际情况呢，我们可能还需要一些额外的功能特性

5. 动态调度（更改规则实时生效）
6. 支持任务的并发执行

## Quartz Java Demo
1. 引入依赖
```xml
    <dependency>
      <groupId>org.quartz-scheduler</groupId>
      <artifactId>quartz</artifactId>
      <version>2.2.1</version>
    </dependency>

    <dependency>
      <groupId>org.quartz-scheduler</groupId>
      <artifactId>quartz-jobs</artifactId>
      <version>2.2.1</version>
    </dependency>
```
2. 一个完整的任务调度需要三个对象： 任务、规则、调度器
* 任务
```java
public class FirstJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	// 工作内容
        System.out.println("吃饭、睡觉、打豆豆。。。" + System.currentTimeMillis());
    }
}
```

```java
JobDetail jobDetail = JobBuilder.newJob(FirstJob.class)
	// 任务的唯一标识（任务名 + 任务组）
	.withIdentity("testJob", "test")
	/// 执行任务时附带的一些参数，此处暂时不需要
	//.usingJobData("name", "树林里面丢了鞋")
	.build();
```
* 规则
```java
Trigger trigger = TriggerBuilder.newTrigger()
	.withIdentity("testTrigger", "test")
	.withSchedule(
		// 一秒执行一次
                SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()
	).build();
```
* 调度器
```java
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
scheduler.start(); // 开始工作
scheduler.scheduleJob(jobDetail, trigger);
```
* 运行结果
```console
吃饭、睡觉、打豆豆。。。1573282083649
吃饭、睡觉、打豆豆。。。1573282084605
吃饭、睡觉、打豆豆。。。1573282085604
吃饭、睡觉、打豆豆。。。1573282086604
吃饭、睡觉、打豆豆。。。1573282087605
吃饭、睡觉、打豆豆。。。1573282088605
```
## Quartz mysql
打开[官网](http://www.quartz-scheduler.org)，点击 downloads 下载解压即可找到 tables_mysql_innodb.sql。
quartz-2.3.0-SNAPSHOT\src\org\quartz\impl\jdbcjobstore\tables_mysql_innodb.sql
```sql
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(190) NOT NULL,
JOB_GROUP VARCHAR(190) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
JOB_NAME VARCHAR(190) NOT NULL,
JOB_GROUP VARCHAR(190) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(190) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(190) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
INSTANCE_NAME VARCHAR(190) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(190) NULL,
JOB_GROUP VARCHAR(190) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(190) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

commit;
```
## Quartz Spring
Spring 应用无非就是把 bean 管理起来，下面给了 Spring 配置示例。
* spring-quartz.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd   
    http://www.springframework.org/schema/context   
    http://www.springframework.org/schema/context/spring-context-4.0.xsd">
    
    <!-- 引入quartz数据库配置文件 -->
    <context:property-placeholder location="classpath:quartz/quartz-db.properties" ignore-unresolvable="true"/>
    
    <!-- quartz数据源 -->
    <bean id="quartzDataSource" class="com.alibaba.druid.pool.DruidDataSource"
        destroy-method="close">
        <property name="driverClassName" value="${quartz.jdbc.driverClassName}" />
        <property name="url" value="${quartz.jdbc.url}" />
        <property name="username" value="${quartz.jdbc.username}" />
        <property name="password" value="${quartz.jdbc.password}" />
    </bean>

    <!-- scheduler -->
    <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean" lazy-init="false">
        <property name="dataSource" ref="quartzDataSource" />
        <property name="autoStartup" value="true"></property>
	<!-- 此处引入了自定义 properties 文件 -->
        <property name="configLocation" value="classpath:quartz/quartz.properties" />
        <property name="jobDetails">
        	<list>
			<!-- 注册 JobDetail 到 scheduler  -->
        		<ref bean="flyleafJobDetail"/>
        	</list>
        </property>
        <property name="triggers">
            	<list>
			<!-- 注册 Trigger 到 scheduler -->
            		<ref bean="flyleafTrigger"/>
            	</list>
        </property>
        <property name="jobFactory">
		<!-- 注意，这个工厂是自定义的工厂，在这里面对 JobDetail 实例进行依赖注入，下面会给出代码示例 -->
        	<bean class="com.bloom.task.AutowiringSpringBeanJobFactory"></bean>
        </property>
    </bean>   

    <!-- JobDetail -->
    <bean id="flyleafJobDetail" class="org.springframework.scheduling.quartz.JobDetailFactoryBean">
        <property name="jobClass" value="com.bloom.task.Flyleaf"/>
		<property name="durability" value="true" />	
		<property name="requestsRecovery" value="true" />
    </bean>

    <!-- Trigger -->
    <bean id="flyleafTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
    	<property name="jobDetail" ref="flyleafJobDetail" />
		<property name="cronExpression" value="0/5 * * * * ?" />
    </bean>
</beans>
```
* quartz-db.properties
```python
quartz.jdbc.driverClassName = ${quartz.jdbc.driverClassName}
quartz.jdbc.url = ${quartz.jdbc.url}
quartz.jdbc.username = ${quartz.jdbc.username}
quartz.jdbc.password = ${quartz.jdbc.password}
```
* quartz.properties
```python
org.quartz.scheduler.instanceName: DefaultQuartzScheduler
org.quartz.scheduler.rmi.export: false
org.quartz.scheduler.rmi.proxy: false
org.quartz.scheduler.wrapJobExecutionInUserTransaction: false

org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount: 10
org.quartz.threadPool.threadPriority: 5
org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread: true

org.quartz.jobStore.misfireThreshold: 60000

org.quartz.jobStore.class: org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.clusterCheckinInterval: 1000

# cluster config
# 开启集群
org.quartz.jobStore.isClustered: true 
# 集群实例ID，使用 AUTO 即可。使用别的必须保证实例 ID 唯一，所以使用 AUTO 就行。
org.quartz.scheduler.instanceId: AUTO 
```
* AutowiringSpringBeanJobFactory（自定义 Job 工厂）
```java
package com.bloom.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class AutowiringSpringBeanJobFactory extends AdaptableJobFactory implements ApplicationContextAware{
	
	private transient AutowireCapableBeanFactory beanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance =  super.createJobInstance(bundle);
		// 此处对 job 实例进行依赖注入
		beanFactory.autowireBean(jobInstance);
		return jobInstance;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

}
```
* FlyLeaf
```java
/**
 * 【飞叶流】
 * @author 树林里面丢了鞋
 *
 */
@DisallowConcurrentExecution // 该注解会禁止任务并发执行
public class Flyleaf extends QuartzJobBean{
	@Autowired // 因为自定义了工厂，可以注入 bean
	private PetalProgressService petalProgressServiceImpl;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info(String.format("%s start:%s", "飞叶流",DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
		
		// doSomeThing();
		
		logger.info(String.format("%s end:%s", "飞叶流",DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
	}

}
```
## Quartz SpringBoot
这个就更简单啦
1. 引入依赖
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-quartz</artifactId>
      <version>2.2.1.RELEASE</version>
    </dependency>
```
2.定义一个 Job （TestService 是为了测试是否能够自动注入）
* MyJob
```java
@DisallowConcurrentExecution // 不允许并发执行
public class MyJob extends QuartzJobBean {
    @Autowired
    TestService testService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        testService.doSomeThing();
    }
}
```
* TestService
```java
@Service
public class TestService {

    public String doSomeThing() {
        System.out.println("do MyJob ..." + System.currentTimeMillis());
        return "success";
    }
}
```
3. 启动类
```java
@SpringBootApplication
@Configuration
public class BootStrapApplication {

    @Bean("myJob")
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "testGroup")
                .storeDurably()
                .build();
    }

    @Bean("myJobTrigger")
    public Trigger myJobTrigger(@Autowired @Qualifier("myJob") JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("myJobTrigger", "testTriggerGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()
                ).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(BootStrapApplication.class, args);
    }
}
```
4. 直接运行
```
2019-11-09 18:27:34.657  INFO 152444 --- [           main] c.g.picker.quartz.BootStrapApplication   : Started BootStrapApplication in 0.891 seconds (JVM running for 2.569)
do MyJob ...1573295254658
do MyJob ...1573295255539
do MyJob ...1573295256530
do MyJob ...1573295257530
do MyJob ...1573295258529
do MyJob ...1573295259530
do MyJob ...1573295260530
```
5. 如果要使用数据库呢，下面提供一个配置文件参考
* application-quartz.yml
```yml
spring:
  quartz:
    auto-startup: true
    job-store-type: jdbc
    jdbc:
      initialize-schema: never
    overwrite-existing-jobs: false
    properties:
      org:
        quartz:
          scheduler:
	    # 同一个集群下的实例 instanceName 必须一致
            instanceName: clusteredScheduler
            # AUTO 就是最好的选择
            instanceId: AUTO
          jobStore:
            # 数据源名称
            dataSource: quartzDataSource
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            # 开启集群
            isClustered: true
            clusterCheckinInterval: 10000
            useProperties: false
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
```
```java
    @Bean("quartzDataSource")
    @Primary // 如果提示没有 quartzDataSource 可以加上该注解。
    @QuartzDataSource
    @ConfigurationProperties(prefix = "quartz.datasource")
    public DataSource quartzDataSource() {
        return new DruidDataSource();
    }
```

## 为什么要部署Quartz集群？
1. 可用性
2. 负载

## 集群部署需要解决的问题？
1. 任务重跑
2. 任务漏跑

```这两个 quartz 自己就可以解决```