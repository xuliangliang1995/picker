package com.grasswort.picker.commons.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author xuliangliang
 * @Classname TimeFormat
 * @Description 时间转换
 * @Date 2019/11/19 17:29
 * @blame Java Team
 */
public class TimeFormat {

    private final static ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    public final static String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";

    public final static String yyyyMMdd = "yyyy-MM-dd";

    /**
     * 格式化当前时间
     * @param pattern
     * @return
     */
    public static String format(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern).withZone(ZONE_ID));
    }

    /**
     * 格式化任一时间
     * @param instant
     * @param pattern
     * @return
     */
    public static String format(Instant instant, String pattern) {
        return instant.atZone(ZONE_ID).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 默认格式化 yyyyMMddHHmm
     * @return
     */
    public static String format() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(yyyyMMddHHmm).withZone(ZONE_ID));
    }

    /**
     * 格式化任一时间
     * @param instant
     * @return
     */
    public static String format(Instant instant) {
        return instant.atZone(ZONE_ID).format(DateTimeFormatter.ofPattern(yyyyMMddHHmm));
    }

}
