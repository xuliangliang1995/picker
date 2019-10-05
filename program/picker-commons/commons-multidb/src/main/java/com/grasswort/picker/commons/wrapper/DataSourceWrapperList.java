package com.grasswort.picker.commons.wrapper;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname DataSourceWrapperList
 * @Description TODO
 * @Date 2019/9/30 10:30
 * @blame Java Team
 */
@Data
@Component
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnMissingBean(DataSourceWrapperList.class)
@ConfigurationProperties(prefix = "multidb")
public class DataSourceWrapperList {

    private String defaultGroup;

    private List<DataSourceWrapper> wrappers;

    public DataSourceWrapperList() {
        if (DataSourceWrapper.aClass == null) {
            DataSourceWrapper.aClass = DruidDataSource.class;
        }
    }

    public DataSourceWrapperList(Class<? extends DataSource> claz) {
        if (DataSourceWrapper.aClass == null) {
            DataSourceWrapper.aClass = claz;
        }
    }
}
