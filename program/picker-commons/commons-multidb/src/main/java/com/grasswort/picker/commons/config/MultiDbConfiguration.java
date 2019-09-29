package com.grasswort.picker.commons.config;

import com.grasswort.picker.commons.constant.DBType;
import com.grasswort.picker.commons.exception.MultiDBException;
import com.grasswort.picker.commons.wrapper.DataSourceWrapper;
import com.grasswort.picker.commons.wrapper.MultiDataSourceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.*;

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


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Lazy
    public MultiDataSourceWrapper dataSource() throws MultiDBException {
        Map<String, DataSourceWrapper> wrappers = applicationContext.getBeansOfType(DataSourceWrapper.class);
        Set<Map.Entry<String, DataSourceWrapper>> entries = wrappers.entrySet();
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
            targetDataSources.put(key, wrapper.getDataSource());
        }

        if (masterKeys.size() == 0) {
            throw new MultiDBException("检测到主库数量为 0，主库的数量至少为 1。");
        }

        DBLocalHolder.MASTER_KEYS.addAll(masterKeys);
        DBLocalHolder.SLAVE_KEYS.addAll(slaveKeys);

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(targetDataSources.get(maxWeightMasterKey));
        log.info("数据源数量：{}", targetDataSources.size());
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        myRoutingDataSource.afterPropertiesSet();

        MultiDataSourceWrapper wrapper = new MultiDataSourceWrapper();
        wrapper.setDataSource(myRoutingDataSource);
        return wrapper;
    }
}
