package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssKeySaveRequest
 * @Description 存储
 * @Date 2019/10/17 22:49
 * @blame Java Team
 */
@Data
public class OssKeySaveRequest extends AbstractRequest {
    /**
     * 存储空间名称
     */
    private String bucketName;
    /**
     * 对象名称
     */
    private String objectKey;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String bucketName;
        private String objectKey;

        private Builder() {
        }

        public static Builder anOssKeySaveRequest() {
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

        public OssKeySaveRequest build() {
            OssKeySaveRequest ossKeySaveRequest = new OssKeySaveRequest();
            ossKeySaveRequest.setBucketName(bucketName);
            ossKeySaveRequest.setObjectKey(objectKey);
            return ossKeySaveRequest;
        }
    }
}
