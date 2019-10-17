package com.grasswort.picker.oss.manager.aliyunoss.bucket;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.StorageClass;
import com.grasswort.picker.oss.manager.aliyunoss.Oss;

/**
 * @author xuliangliang
 * @Classname CreateBucket
 * @Description 阿里云OSS对象存储空间创建
 * @Date 2019/10/17 21:38
 * @blame Java Team
 */
public class CreateBucket {
    private CreateBucket() {};
    /**
     * 获取实例（单例）
     *@author xuliangliang
     *@return
     */
    public static CreateBucket instance() {
        return LazyHolder.sington;
    }

    /**
     * 创建存储库（默认标准、公共读）
     * @param bucketName
     * @return
     */
    public Bucket create(String bucketName) {
        return createStandardPublicReadBucket(bucketName);
    }

    /**
     * 创建标准私有库
     * @param bucketName
     */
    public Bucket createStandardPrivateBucket(String bucketName) {
        OSSClient oss = Oss.client();
        return oss.createBucket("bucketName");
    }

    /**
     * 创建标准读库
     * @param bucketName
     * @return
     */
    public Bucket createStandardPublicReadBucket(String bucketName) {
        OSSClient oss = Oss.client();
        CreateBucketRequest r = new CreateBucketRequest(bucketName);
        r.setCannedACL(CannedAccessControlList.PublicRead);
        r.setStorageClass(StorageClass.Standard);
        return oss.createBucket(r);
    }

    /**
     * 创建标准公共读写库
     * @param bucketName
     */
    public Bucket createStandardPublicBucket(String bucketName) {
        OSSClient oss = Oss.client();
        try {
            CreateBucketRequest r = new CreateBucketRequest(bucketName);
            r.setCannedACL(CannedAccessControlList.PublicReadWrite);
            r.setStorageClass(StorageClass.Standard);
            return oss.createBucket(r);
        } finally {
            oss.shutdown();
        }
    }
    /**
     * 单例
     * @author xuliangliang
     *
     */
    private static class LazyHolder {
        static final CreateBucket sington = new CreateBucket();
    }

}
