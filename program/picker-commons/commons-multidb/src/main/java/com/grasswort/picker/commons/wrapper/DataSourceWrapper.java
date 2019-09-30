package com.grasswort.picker.commons.wrapper;

import com.grasswort.picker.commons.constant.DBType;
import com.grasswort.picker.commons.exception.MultiDBException;
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

    protected static Class<? extends DataSource> aClass;
    /**
     * 必须具备无参构造方法
     */
    private DataSource dataSource;

    private DBType dbType;

    private int weight = 1;

    /**
     * Constructor
     */
    public DataSourceWrapper() {
        instanceDataSource();
    }

    /**
     * 指定 DataSource 实例化的类
     * @param clas
     */
    public DataSourceWrapper(Class clas) {
        aClass = clas;
        instanceDataSource();
    }
    /**
     * 如果 dataSource 不具备无参构造，则由此注入
     * @param dataSource
     */
    public DataSourceWrapper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 初始化一个 dataSource 实例，不实例化将无法进行参数注入
     */
    private void instanceDataSource() {
        if (dataSource == null) {
            if (aClass == null) {
                throw new MultiDBException("尚未指定 dataSource 实例化的类型。");
            }
            try {
                dataSource = aClass.newInstance();
                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            throw new MultiDBException("dataSource 实例化的类型必须具备无参构造方法。");
        }
    }
}
