package com.grasswort.picker.user.config.db;

import com.grasswort.picker.user.constants.DBEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xuliangliang
 * @Classname DBContext
 * @Description TODO
 * @Date 2019/9/29 16:58
 * @blame Java Team
 */
@Slf4j
public class DBLocalHolder {

    private static final ThreadLocal<DBEnum> holder = new ThreadLocal<>();

    /**
     * 选择主库
     */
    public static void master() {
        holder.set(DBEnum.MASTER);
    }

    /**
     * 选择从库
     */
    public static void slave() {
        holder.set(DBEnum.SLAVE_1);
    }

    /**
     * 获取
     * @return
     */
    public static DBEnum get() {
        DBEnum db = holder.get();
        log.info("切换数据源：{}", db);
        return db;
    }
}
