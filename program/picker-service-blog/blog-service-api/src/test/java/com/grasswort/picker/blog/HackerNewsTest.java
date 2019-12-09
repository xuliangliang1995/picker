package com.grasswort.picker.blog;

/**
 * @author xuliangliang
 * @Classname Test
 * @Description TODO
 * @Date 2019/12/7 17:49
 * @blame Java Team
 */
public class HackerNewsTest {

    public static void main(String[] args) {
        System.out.println(score(100, 2));
        System.out.println(score(60, 1));
    }

    public static double score(double p, double t) {
        //r=(P – 1) / (t + 2)^1.8;
        // P 得票数
        // t 时间（天）
        return (p - 1) / Math.pow(t + 2, 1.8);
    }
}
