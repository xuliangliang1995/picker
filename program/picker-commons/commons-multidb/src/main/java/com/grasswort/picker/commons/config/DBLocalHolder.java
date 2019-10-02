package com.grasswort.picker.commons.config;

import com.grasswort.picker.commons.exception.MultiDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    protected static final Map<String, List<String>> GROUP_KEY_MAP = new ConcurrentHashMap<>();

    protected static String DEFAULT_GROUP = "MASTER";

    /**
     * 随机
     */
    private static final Random random = new Random();

    /**
     * 选择数据库组
     */
    public static void selectDBGroup(String group) {
        List<String> dbKeys = GROUP_KEY_MAP.get(group);
        if (dbKeys == null || dbKeys.isEmpty()) {
            return ;
        }
        HOLDER.set(dbKeys.get(random.nextInt(dbKeys.size())));
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
            DBLocalHolder.selectDBGroup(DEFAULT_GROUP);
            key = HOLDER.get();
            if (StringUtils.isEmpty(key)) {
                if (StringUtils.isEmpty(DEFAULT_GROUP)) {
                    throw new MultiDBException("未指定默认数据源组");
                }
                if (GROUP_KEY_MAP.get(DEFAULT_GROUP) == null || GROUP_KEY_MAP.get(DEFAULT_GROUP).isEmpty()) {
                    throw new MultiDBException("默认数据源配置个数不能为 0");
                }
            }
        }
        log.info("切换数据源：{}", key);
        return key;
    }

}
