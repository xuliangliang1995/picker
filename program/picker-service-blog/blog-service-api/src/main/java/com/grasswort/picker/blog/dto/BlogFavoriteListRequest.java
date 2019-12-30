package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteListRequest.java
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Data
public class BlogFavoriteListRequest extends AbstractRequest {
    @NotEmpty
    private Long authorId;
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
        private Long authorId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aBlogFavoriteListRequest() {
            return new Builder();
        }

        public Builder withAuthorId(Long authorId) {
            this.authorId = authorId;
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

        public BlogFavoriteListRequest build() {
            BlogFavoriteListRequest blogFavoriteListRequest = new BlogFavoriteListRequest();
            blogFavoriteListRequest.setAuthorId(authorId);
            blogFavoriteListRequest.setPageNo(pageNo);
            blogFavoriteListRequest.setPageSize(pageSize);
            return blogFavoriteListRequest;
        }
    }
}
