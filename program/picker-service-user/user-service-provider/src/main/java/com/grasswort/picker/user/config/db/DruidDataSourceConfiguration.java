package com.grasswort.picker.user.config.db;

import com.grasswort.picker.commons.constant.ConstantMultiDB;
import com.grasswort.picker.commons.wrapper.MultiDataSourceWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final static String MAPPER_XML_LOCATION = "classpath:com.grasswort.picker.user.dao.persistence.*Mapper.xml";

    @Bean(name = "dataSource")
    public DataSource dataSource(@Autowired @Qualifier(ConstantMultiDB.MULIDB_DATA_SOURCE_WRAPPER_BEAN_NAME) MultiDataSourceWrapper wrapper) {
        return wrapper.getDataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

}
