package com.grasswort.picker.commons.time;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xuliangliang
 * @Classname TimeFormat
 * @Description 时间转换
 * @Date 2019/11/19 17:29
 * @blame Java Team
 */
public class TimeFormat {

    public final static ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");//Asia/Shanghai

    public final static String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";

    public final static String yyyyMMdd = "yyyy-MM-dd";

    public final static DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withZone(ZONE_SHANGHAI);

    /**
     * 格式化当前时间
     * @param pattern
     * @return
     */
    public static String format(String pattern) {
        return LocalDateTime.now(ZONE_SHANGHAI).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化任一时间
     * @param instant
     * @param pattern
     * @return
     */
    public static String format(Instant instant, String pattern) {
        return instant.atZone(ZONE_SHANGHAI).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 默认格式化 yyyyMMddHHmm
     * @return
     */
    public static String format() {
        return LocalDateTime.now(ZONE_SHANGHAI).format(DateTimeFormatter.ofPattern(yyyyMMddHHmm));
    }

    /**
     * 格式化任一时间
     * @param instant
     * @return
     */
    public static String format(Instant instant) {
        return instant.atZone(ZONE_SHANGHAI).format(DateTimeFormatter.ofPattern(yyyyMMddHHmm));
    }

}
