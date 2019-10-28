package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssRefRequest
 * @Description 引用请求
 * @Date 2019/10/17 21:19
 * @blame Java Team
 */
@Data
public class OssRefRequest extends AbstractRequest {
    /**
     * 存储空间名称
     */
    private String bucketName;
    /**
     * 对象名称
     */
    private String objectKey;
    /**
     * 删除密钥
     */
    private String delKey;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String bucketName;
        private String objectKey;
        private String delKey;

        private Builder() {
        }

        public static Builder anOssRefRequest() {
            return new Builder();
        }

        public Builder withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public Builder withObjectKey(String objectKey) {
            this.objectKey = objectKey;
            return this;
        }

        public Builder withDelKey(String delKey) {
            this.delKey = delKey;
            return this;
        }

        public OssRefRequest build() {
            OssRefRequest ossRefRequest = new OssRefRequest();
            ossRefRequest.setBucketName(bucketName);
            ossRefRequest.setObjectKey(objectKey);
            ossRefRequest.setDelKey(delKey);
            return ossRefRequest;
        }
    }
}
