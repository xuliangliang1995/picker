package com.grasswort.picker.user.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.commons.constant.ConstantMultiDB;
import com.grasswort.picker.commons.wrapper.DataSourceWrapperList;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
    private final static String DATA_SOURCE_WRAPPERS_PREFIX = "spring.dbwrappers";
    private final static String MAPPER_XML_LOCATION = "classpath:com.grasswort.picker.user.dao.persistence.*Mapper.xml";


    @Bean
    @ConfigurationProperties(prefix = DATA_SOURCE_WRAPPERS_PREFIX)
    public DataSourceWrapperList masterDataSourceWrapper() {
        return new DataSourceWrapperList(DruidDataSource.class);
    }

    @Bean
    @Lazy
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier(ConstantMultiDB.MULTIDB_DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

}
