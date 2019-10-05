package com.grasswort.picker.commons.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.commons.constant.ConstantMultiDB;
import com.grasswort.picker.commons.exception.MultiDBException;
import com.grasswort.picker.commons.wrapper.DataSourceWrapper;
import com.grasswort.picker.commons.wrapper.DataSourceWrapperList;
import com.grasswort.picker.commons.wrapper.MultiDataSourceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuliangliang
 * @Classname MultiDbConfiguration
 * @Description 多数据源配置文件
 * @Date 2019/9/29 18:23
 * @blame Java Team
 */
@Slf4j
@Configuration
@ComponentScan({
        "com.grasswort.picker.commons.aspect",
        "com.grasswort.picker.commons.wrapper"
})
@EnableConfigurationProperties({DataSourceWrapperList.class})
public class MultiDbConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    /*@Bean
    @ConditionalOnClass(DruidDataSource.class)

    public DataSourceWrapperList dataSourceWrapperList() {
        return new DataSourceWrapperList(DruidDataSource.class);
    }*/

    @Bean(name = ConstantMultiDB.MULIDB_DATA_SOURCE_WRAPPER_BEAN_NAME)
    @ConditionalOnBean({DataSourceWrapperList.class})
    public MultiDataSourceWrapper dataSourceWrapper1() {
        DataSource dataSource = generateMyRoutingDataSource(getWrapperMapByDataSourceWrapperList());
        return wrapperMultiDataSource(dataSource);
    }


    /**
     * 根据 wrapperList 获取 wrapperMap
     * @return
     */
    private Map<String, DataSourceWrapper> getWrapperMapByDataSourceWrapperList() {
        DataSourceWrapperList wrapperList = applicationContext.getBean(DataSourceWrapperList.class);
        List<DataSourceWrapper> wrappers = wrapperList.getWrappers();
        Map<String, DataSourceWrapper> wrapperMap = new HashMap<>();
        // 设置默认数据库
        String defaultGroup = wrapperList.getDefaultGroup();
        if (StringUtils.isEmpty(defaultGroup)) {
            defaultGroup = DBLocalHolder.DEFAULT_GROUP;
        }
        DBLocalHolder.DEFAULT_GROUP = defaultGroup;
        // 生成数据源的 key
        final Map<String, AtomicInteger> atomicIntegerMap =  new HashMap<>();
        wrappers.forEach(wrapper -> {
            String group = wrapper.getGroup();
            if (! atomicIntegerMap.containsKey(group)) {
                atomicIntegerMap.put(group, new AtomicInteger());
            }
            wrapperMap.put(String.format("%s-%s", group, atomicIntegerMap.get(group).incrementAndGet()), wrapper);
        });
        return wrapperMap;
    }



    /**
     * 生成 routingDataSource
     * @param wrapperMap
     * @return
     */
    private DataSource generateMyRoutingDataSource(Map<String, DataSourceWrapper> wrapperMap) {
        Set<Map.Entry<String, DataSourceWrapper>> entries = wrapperMap.entrySet();
        Map<String, List<String>> groupKeys = DBLocalHolder.GROUP_KEY_MAP;
        final String defaultGroup = DBLocalHolder.DEFAULT_GROUP;

        Map<Object, Object> targetDataSources = new HashMap<>();
        for (Map.Entry<String, DataSourceWrapper> entry: entries) {
            String key = entry.getKey();
            DataSourceWrapper wrapper = entry.getValue();
            int weight = wrapper.getWeight();
            if (weight < 0 || weight > 10) {
                throw new MultiDBException("数据库权重请配置在 0 ~ 10 之间。配置为 0 的将会被忽略。");
            }
            if (weight == 0) {
                continue;
            }

            String group = wrapper.getGroup();
            if (weight > 0) {
                if (! groupKeys.containsKey(group)) {
                    groupKeys.put(group, new ArrayList<>());
                }
                for (int i = 0; i < weight ; i++) {
                    groupKeys.get(group).add(key);
                }
            }

            log.info("检测到数据源：{}，权重：{}", key, weight);
            targetDataSources.put(key, wrapper.getDataSource());
        }

        if (groupKeys.get(defaultGroup) == null || groupKeys.get(defaultGroup).isEmpty()) {
            throw new MultiDBException(String.format("检测到主库[%s]数量为 0，主库的数量至少为 1。", defaultGroup));
        }

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get(groupKeys.get(defaultGroup).get(0)));
        log.info("数据源总数量：{}", targetDataSources.size());
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        myRoutingDataSource.afterPropertiesSet();
        return myRoutingDataSource;
    }

    /**
     * 封装了 dataSource
     * @param dataSource
     * @return
     */
    private MultiDataSourceWrapper wrapperMultiDataSource(DataSource dataSource) {
        MultiDataSourceWrapper wrapper = new MultiDataSourceWrapper();
        wrapper.setDataSource(dataSource);
        return wrapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /// 自动封装成 DataSource 代码，如果项目还有其他的 DataSource, 可能会造成 Spring 循环依赖
    /*@Bean(name = ConstantMultiDB.MULTIDB_DATA_SOURCE_BEAN_NAME)
    @Lazy
    @ConditionalOnBean(DataSourceWrapperList.class)
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource1() {
        return generateMyRoutingDataSource(getWrapperMapByDataSourceWrapperList());
    }

    @Bean(name = ConstantMultiDB.MULTIDB_DATA_SOURCE_BEAN_NAME)
    @Lazy
    @ConditionalOnMissingBean({DataSourceWrapperList.class, DataSource.class})
    public DataSource dataSource2() {
        return generateMyRoutingDataSource(getWrapperMapByDataSourceWrapper());
    }
    @Bean(name = ConstantMultiDB.MULIDB_DATA_SOURCE_WRAPPER_BEAN_NAME)
    @Lazy
    @ConditionalOnMissingBean(DataSourceWrapperList.class)
    public MultiDataSourceWrapper dataSourceWrapper2() {
        DataSource dataSource = generateMyRoutingDataSource(getWrapperMapByDataSourceWrapper());
        return wrapperMultiDataSource(dataSource);
    }

    */

    /**
     * 根据 wrapper 获取 wrapperMap
     * @return
     */
    /*private Map<String, DataSourceWrapper> getWrapperMapByDataSourceWrapper() {
        Map<String, DataSourceWrapper> wrapperMap = applicationContext.getBeansOfType(DataSourceWrapper.class);
        return wrapperMap;
    }*/
}
