package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssRefDelRequest
 * @Description 删除引用
 * @Date 2019/10/17 23:24
 * @blame Java Team
 */
@Data
public class OssRefDelRequest extends AbstractRequest {
    /**
     * 引用的 id
     */
    private Long refId;
    /**
     * 执行操作的密钥
     */
    private String delKey;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private Long refId;
        private String delKey;

        private Builder() {
        }

        public static Builder anOssRefDelRequest() {
            return new Builder();
        }

        public Builder withRefId(Long refId) {
            this.refId = refId;
            return this;
        }

        public Builder withDelKey(String delKey) {
            this.delKey = delKey;
            return this;
        }

        public OssRefDelRequest build() {
            OssRefDelRequest ossRefDelRequest = new OssRefDelRequest();
            ossRefDelRequest.setRefId(refId);
            ossRefDelRequest.setDelKey(delKey);
            return ossRefDelRequest;
        }
    }
}
