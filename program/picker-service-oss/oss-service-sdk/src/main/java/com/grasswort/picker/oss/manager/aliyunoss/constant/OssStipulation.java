package com.grasswort.picker.oss.manager.aliyunoss.constant;

/**
 * @author xuliangliang
 * @Classname OssStipulation
 * @Description 本项目OSS相关约束规定(规范存储)
 * @Date 2019/10/17 21:40
 * @blame Java Team
 */
public class OssStipulation {

    /**
     *  所有key生成策略为System.currentTimeMillis() + 线程名称 + 随机字符串）经MD5加密后生成的32位字符串
     */
    public static final int OSS_KEY_NAME_LENGTH = 41;
    /**
     *  匹配 OSS_URL 的正则表达式
     *  https://[bucket].oss-cn-hangzhou.aliyuncs.com/[20190909]/[32位字符串].[suffix]_[压缩策略]
     */
    public static final String OSS_URL_REGEX = "https:\\/\\/[a-z-]*.oss-cn-beijing.aliyuncs.com\\/[0-9]{8}\\/[a-z0-9]{32}\\.[a-z]*";
    /**
     * 图片地址后缀
     */
    public static final String HOST_SUFFIX = ".oss-cn-beijing.aliyuncs.com";
    /**
     *  默认存储空间
     */
    public static final String DEFAULT_BUCKET_NAME = "picker-oss";
    /**
     * OSS路径模版【三个%s占位符分别表示存储空间、对象名称、图片处理策略】
     */
    public static final String OSS_URL_TEMPLATE = "https://%s.oss-cn-beijing.aliyuncs.com/%s%s";
    /**
     * OSS路径模版前缀
     */
    public static final String OSS_URL_PREFIX_TEMPLATE = "https://%s.oss-cn-beijing.aliyuncs.com/";

    /**
     * @author xuliangliang
     * @Classname OssStipulation
     * @Description 默认存储空间的图片处理策略
     * @Date 2019/10/17 21:40
     * @blame Java Team
     */
    public static final class DefaultBucketDisposeStyle {
        /**
         * 自定义策略-适度压缩
         */
        public static final String TARGET = "_target";
        /**
         * 自定义策略-过度压缩
         */
        public static final String COMPRESS = "_compress";
    }
}
