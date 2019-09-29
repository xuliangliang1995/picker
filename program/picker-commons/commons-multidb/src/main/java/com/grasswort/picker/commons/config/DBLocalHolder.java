package com.grasswort.picker.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xuliangliang
 * @Classname DBLocalHolder
 * @Description 数据库选择器
 * @Date 2019/9/29 18:19
 * @blame Java Team
 */
@Slf4j
public class DBLocalHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();
    /**
     * master datasource keys
     */
    protected static final List<String> MASTER_KEYS = new ArrayList<>();
    /**
     * slave datasource keys
     */
    protected static final List<String> SLAVE_KEYS = new ArrayList<>();
    /**
     * 随机
     */
    private static final Random random = new Random();

    /**
     * 选择主库
     */
    public static void master() {
        HOLDER.set(MASTER_KEYS.get(random.nextInt(MASTER_KEYS.size())));
    }
    /**
     * 选择从库
     */
    public static void slave() {
        if (SLAVE_KEYS.size() > 0) {
            HOLDER.set(SLAVE_KEYS.get(random.nextInt(SLAVE_KEYS.size())));
        }
    }

    /**
     * 清空
     */
    public static void clear() {
        HOLDER.remove();
    }
    /**
     * 获取当前线程持有的 key
     * @return
     */
    public static String get() {
        String key = HOLDER.get();
        if (StringUtils.isEmpty(key)) {
            DBLocalHolder.master();
            key = HOLDER.get();
        }
        log.info("切换数据源：{}", key);
        return key;
    }

}
