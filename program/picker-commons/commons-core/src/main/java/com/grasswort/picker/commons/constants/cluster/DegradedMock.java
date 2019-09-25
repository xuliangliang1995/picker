package com.grasswort.picker.commons.constants.cluster;

/**
 * @author xuliangliang
 * @Classname DegradedMock
 * @Description 服务失败降级
 * @Date 2019/9/25 22:58
 * @blame Java Team
 */
public class DegradedMock {
    /**
     * 返回返回类型的默认值或集合的空值
     */
    public static final String
            FAIL_EMPTY = "fail:return empty",
            FORCE_EMPTY = "force:return empty";
    /**
     * 返回 null
     */
    public static final String
            FAIL_NULL = "fail:return null",
            FORCE_NULL = "force:return null";
    /**
     * 返回 true
     */
    public static final String
            FAIL_TRUE = "fail:return true",
            FORCE_TRUE = "force:return true";
    /**
     * 返回 false
     */
    public static final String
            FAIL_FALSE = "fail:return false",
            FORCE_FALSE = "force:return false";
    /**
     * 抛出 RpcException
     */
    public static final String
            FAIL_THROW = "fail:throw",
            FORCE_THROW = "force:throw";

}
