package com.grasswort.picker.commons.wrapper;

import com.grasswort.picker.commons.constant.DBType;
import lombok.Data;

import javax.sql.DataSource;

/**
 * @author xuliangliang
 * @Classname DataSourceWrapper
 * @Description 封装了数据源，添加了数据源类型和权重
 * @Date 2019/9/29 18:18
 * @blame Java Team
 */
@Data
public class DataSourceWrapper {

    private DataSource dataSource;

    private DBType dbType;

    private int weight = 1;

}
