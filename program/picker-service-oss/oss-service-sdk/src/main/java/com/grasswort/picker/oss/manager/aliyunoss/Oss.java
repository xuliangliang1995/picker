package com.grasswort.picker.oss.manager.aliyunoss;

import com.aliyun.oss.OSSClient;
import com.grasswort.picker.oss.manager.aliyunoss.bucket.BucketQuery;
import com.grasswort.picker.oss.manager.aliyunoss.bucket.CreateBucket;
import com.grasswort.picker.oss.manager.aliyunoss.bucket.DeleteBucket;
import com.grasswort.picker.oss.manager.aliyunoss.file.FileDelete;
import com.grasswort.picker.oss.manager.aliyunoss.file.FileUpload;

/**
 * @author xuliangliang
 * @Classname Oss
 * @Description 阿里云OSS文件管理
 * @Date 2019/10/17 21:45
 * @blame Java Team
 */
public class Oss {
    /**
     *
     * <p>Title: client</p>
     * <p>Description: 获取AliyunOss client实例（单例）</p>
     * @return
     * OSSClient
     */
    public static OSSClient client() {
        return LazyHolder.CLIENT;
    }

    /**
     *
     * <p>Title: Oss.java<／p>
     * <p>Description: 阿里云OSS存储空间管理类<／p>
     * <p>Copyright: Copyright (c) 2019<／p>
     * <p>Company: grasswort<／p>
     *
     * @author 树林里面丢了鞋
     * @date 2019年1月4日
     * @version 1.0
     */
    public static class BucketHandler {
        public static final BucketQuery QUERY = BucketQuery.instance();
        public static final CreateBucket CREATE = CreateBucket.instance();
        public static final DeleteBucket DELETE =  DeleteBucket.instance();
    }
    /**
     *
     * <p>Title: Oss.java<／p>
     * <p>Description: 阿里云OSS文件管理<／p>
     * <p>Copyright: Copyright (c) 2019<／p>
     * <p>Company: grasswort<／p>
     *
     * @author 树林里面丢了鞋
     * @date 2019年1月4日
     * @version 1.0
     */
    public static class FileHandler {
        public static final FileUpload UPLOAD = FileUpload.instance();
        public static final FileDelete DELETE = FileDelete.instance();
    }

    /**
     *
     * <p>Title: Oss.java<／p>
     * <p>Description: 利用JVM虚拟机初始化类机制来实现延迟懒加载单例模式<／p>
     * <p>Copyright: Copyright (c) 2019<／p>
     * <p>Company: grasswort<／p>
     *
     * @author 树林里面丢了鞋
     * @date 2019年1月4日
     * @version 1.0
     */
    private static class LazyHolder {
        static final String END_POINT = "http://oss-cn-beijing.aliyuncs.com";
        static final String ACCESS_KEY_ID = "LTAIozvEu5kWwJR4";
        static final String ACCESS_KEY_SECRET = "2lzWjrIitqESLVykOVfNzWcyD8a0gl";
        static final OSSClient CLIENT = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    };
}
