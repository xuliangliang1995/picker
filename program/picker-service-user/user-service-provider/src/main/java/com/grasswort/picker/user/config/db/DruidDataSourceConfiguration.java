package com.grasswort.picker.user.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.user.constants.DBEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname DruidDataSource
 * @Description 数据源
 * @Date 2019/9/29 16:01
 * @blame Java Team
 */
@Configuration
public class DruidDataSourceConfiguration {
    private final static String MASTER = "masterDataSource";
    private final static String MASTER_PREFIX = "spring.datasource.master";
    private final static String SLAVE_1 = "slaveDataSource1";
    private final static String SLAVE_1_PREFIX = "spring.datasource.slave1";
    private final static String DATA_SOURCE = "dataSource";
    private final static String MAPPER_XML_LOCATION = "classpath:com.grasswort.picker.user.dao.persistence.*Mapper.xml";

    @Bean(name = MASTER)
    @ConfigurationProperties(prefix = MASTER_PREFIX)
    public DataSource masterDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = SLAVE_1)
    @ConfigurationProperties(prefix = SLAVE_1_PREFIX)
    public DataSource slaveDataSource1() {
        return new DruidDataSource();
    }

    @Bean(name = DATA_SOURCE)
    public DataSource myRoutingDataSource(
            @Autowired @Qualifier(MASTER) DataSource masterDataSource,
            @Autowired @Qualifier(SLAVE_1) DataSource slaveDataSource1
    ) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBEnum.MASTER, masterDataSource);
        targetDataSources.put(DBEnum.SLAVE_1, slaveDataSource1);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier(DATA_SOURCE) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

}
