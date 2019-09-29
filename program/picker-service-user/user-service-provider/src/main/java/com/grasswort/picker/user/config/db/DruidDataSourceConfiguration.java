package com.grasswort.picker.user.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.commons.wrapper.DataSourceWrapper;
import com.grasswort.picker.commons.wrapper.MultiDataSourceWrapper;
import com.grasswort.picker.commons.constant.DBType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

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

    @Bean
    public DataSourceWrapper masterDataSourceWrapper(@Autowired @Qualifier(MASTER) DataSource dataSource) {
        DataSourceWrapper wrapper = new DataSourceWrapper();
        wrapper.setDataSource(dataSource);
        wrapper.setDbType(DBType.MASTER);
        wrapper.setWeight(1);
        return wrapper;
    }

    @Bean
    public DataSourceWrapper slaveDataSourceWrapper(@Autowired @Qualifier(SLAVE_1) DataSource dataSource) {
        DataSourceWrapper wrapper = new DataSourceWrapper();
        wrapper.setDataSource(dataSource);
        wrapper.setDbType(DBType.SLAVE);
        wrapper.setWeight(1);
        return wrapper;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(MultiDataSourceWrapper wrapper) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(wrapper.getDataSource());
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

}
