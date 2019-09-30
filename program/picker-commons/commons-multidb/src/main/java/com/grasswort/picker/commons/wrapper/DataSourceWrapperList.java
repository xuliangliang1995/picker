package com.grasswort.picker.commons.wrapper;

import lombok.Data;

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
public class DataSourceWrapperList {

    private List<DataSourceWrapper> wrappers;

    public DataSourceWrapperList(Class<? extends DataSource> claz) {
        if (DataSourceWrapper.aClass == null) {
            DataSourceWrapper.aClass = claz;
        }
    }
}
