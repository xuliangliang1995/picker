package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname MyTopicListRequest
 * @Description
 * @Date 2019/12/10 15:48
 * @blame Java Team
 */
@Data
public class MyTopicListRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long pkUserId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aMyTopicListRequest() {
            return new Builder();
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withPageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public MyTopicListRequest build() {
            MyTopicListRequest myTopicListRequest = new MyTopicListRequest();
            myTopicListRequest.setPkUserId(pkUserId);
            myTopicListRequest.setPageNo(pageNo);
            myTopicListRequest.setPageSize(pageSize);
            return myTopicListRequest;
        }
    }
}
