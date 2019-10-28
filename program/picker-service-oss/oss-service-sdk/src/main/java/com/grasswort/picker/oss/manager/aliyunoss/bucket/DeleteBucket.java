package com.grasswort.picker.oss.manager.aliyunoss.bucket;

import com.aliyun.oss.OSSClient;
import com.grasswort.picker.oss.manager.aliyunoss.Oss;

/**
 * @author xuliangliang
 * @Classname DeleteBucket
 * @Description 阿里云OSS对象存储空间删除
 * @Date 2019/10/17 21:39
 * @blame Java Team
 */
public class DeleteBucket {
    private DeleteBucket() {};

    /**
     * 获取实例（单例）
     *@author xuliangliang
     *@return
     */
    public static DeleteBucket instance() {
        return LazyHolder.sington;
    }

    /**
     * 删除存储仓库
     * @param bucketName
     */
    public void delete(String bucketName) {
        OSSClient oss = Oss.client();
        oss.deleteBucket(bucketName);
    }
    /**
     * 延迟单例
     * @author xuliangliang
     *
     */
    private static class LazyHolder {
        static final DeleteBucket sington = new DeleteBucket();
    }
}
