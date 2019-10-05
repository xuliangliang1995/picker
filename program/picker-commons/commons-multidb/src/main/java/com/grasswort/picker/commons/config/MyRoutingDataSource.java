package com.grasswort.picker.commons.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author xuliangliang
 * @Classname MyRoutingDataSource
 * @Description 多数据源
 * @Date 2019/9/29 19:00
 * @blame Java Team
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DBLocalHolder.get();
    }
}
