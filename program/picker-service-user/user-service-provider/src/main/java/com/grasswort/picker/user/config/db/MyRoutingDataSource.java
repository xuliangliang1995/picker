package com.grasswort.picker.user.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author xuliangliang
 * @Classname MyRoutingDataSource
 * @Description TODO
 * @Date 2019/9/29 16:51
 * @blame Java Team
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DBLocalHolder.get();
    }
}
