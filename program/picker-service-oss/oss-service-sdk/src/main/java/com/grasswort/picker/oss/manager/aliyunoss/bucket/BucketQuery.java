package com.grasswort.picker.oss.manager.aliyunoss.bucket;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.grasswort.picker.oss.manager.aliyunoss.Oss;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BucketQuery
 * @Description 阿里云实例存储空间查询
 * @Date 2019/10/17 21:37
 * @blame Java Team
 */
public class BucketQuery {
    private BucketQuery() {};

    /**
     * 获取实例（单例）
     *@author xuliangliang
     *@return
     */
    public static BucketQuery instance() {
        return LazyHolder.sington;
    }

    /**
     * 存储空间列表
     * @return
     */
    public List<Bucket> buckets(){
        OSSClient oss = Oss.client();
        return oss.listBuckets();
    }

    /**
     * 是否存在该存储空间
     * @param bucketName
     * @return
     */
    public boolean exists(String bucketName) {
        OSSClient oss = Oss.client();
        return oss.doesBucketExist(bucketName);
    }

    /**
     * 延迟单例
     * @author xuliangliang
     *
     */
    private static class LazyHolder {
        static final BucketQuery sington = new BucketQuery();
    }
}
