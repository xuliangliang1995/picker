package com.grasswort.picker.blog.service.heat;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xuliangliang
 * @Classname HackerNewsHeat
 * @Description HackerNews 热度
 * @Date 2019/12/9 9:57
 * @blame Java Team
 */
public class HackerNewsHeat {
    private static final double G = 1.8;
    /**
     * 计算热度
     */
    public static double calculate(long p, long t) {
        return (p - 1) / Math.pow(t + 2, G);
    }
}
