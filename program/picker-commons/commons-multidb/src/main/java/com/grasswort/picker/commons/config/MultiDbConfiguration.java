package com.grasswort.picker.commons.config;

import com.grasswort.picker.commons.constant.ConstantMultiDB;
import com.grasswort.picker.commons.constant.DBType;
import com.grasswort.picker.commons.exception.MultiDBException;
import com.grasswort.picker.commons.wrapper.DataSourceWrapper;
import com.grasswort.picker.commons.wrapper.DataSourceWrapperList;
import com.grasswort.picker.commons.wrapper.MultiDataSourceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
public class MultiDbConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean(name = ConstantMultiDB.MULTIDB_DATA_SOURCE_BEAN_NAME)
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
    @ConditionalOnBean({DataSourceWrapperList.class, DataSource.class})
    public MultiDataSourceWrapper dataSourceWrapper1() {
        DataSource dataSource = generateMyRoutingDataSource(getWrapperMapByDataSourceWrapperList());
        return wrapperMultiDataSource(dataSource);
    }

    @Bean(name = ConstantMultiDB.MULIDB_DATA_SOURCE_WRAPPER_BEAN_NAME)
    @Lazy
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(DataSourceWrapperList.class)
    public MultiDataSourceWrapper dataSourceWrapper2() {
        DataSource dataSource = generateMyRoutingDataSource(getWrapperMapByDataSourceWrapper());
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
        AtomicInteger master = new AtomicInteger();
        AtomicInteger slave = new AtomicInteger();
        wrappers.forEach(wrapper -> {
            if (DBType.MASTER.equals(wrapper.getDbType())) {
                wrapperMap.put(String.format("%s-%s", DBType.MASTER, master.incrementAndGet()), wrapper);
            } else if (DBType.SLAVE.equals(wrapper.getDbType())) {
                wrapperMap.put(String.format("%s-%s", DBType.SLAVE, slave.incrementAndGet()), wrapper);
            }
        });
        return wrapperMap;
    }

    /**
     * 根据 wrapper 获取 wrapperMap
     * @return
     */
    private Map<String, DataSourceWrapper> getWrapperMapByDataSourceWrapper() {
        Map<String, DataSourceWrapper> wrapperMap = applicationContext.getBeansOfType(DataSourceWrapper.class);
        return wrapperMap;
    }

    /**
     * 生成 routingDataSource
     * @param wrapperMap
     * @return
     */
    private DataSource generateMyRoutingDataSource(Map<String, DataSourceWrapper> wrapperMap) {
        Set<Map.Entry<String, DataSourceWrapper>> entries = wrapperMap.entrySet();
        List<String> masterKeys = new ArrayList<>();
        List<String> slaveKeys = new ArrayList<>();
        String maxWeightMasterKey = null;
        int maxMasterWeight = 0;

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
            boolean isMasterDB = DBType.MASTER.equals(wrapper.getDbType());
            if (isMasterDB) {
                if (weight > maxMasterWeight) {
                    maxWeightMasterKey = key;
                }
                for (int i = 0; i < weight; i++) {
                    // 重复添加多次是为了增加随机权重
                    masterKeys.add(key);
                }
            }
            boolean isSlaveDB = DBType.SLAVE.equals(wrapper.getDbType());
            if (isSlaveDB) {
                for (int i = 0; i < weight; i++) {
                    slaveKeys.add(key);
                }
            }
            log.info("检测到数据源：{}，权重：{}", key, weight);
            targetDataSources.put(key, wrapper.getDataSource());
        }

        if (masterKeys.size() == 0) {
            throw new MultiDBException("检测到主库数量为 0，主库的数量至少为 1。");
        }

        DBLocalHolder.MASTER_KEYS.addAll(masterKeys);
        DBLocalHolder.SLAVE_KEYS.addAll(slaveKeys);

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get(maxWeightMasterKey));
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
}
