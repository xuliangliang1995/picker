package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserInteractionDataRequest
 * @Description 用户交互数据查询
 * @Date 2019/12/3 23:34
 * @blame Java Team
 */
@Data
public class UserInteractionDataRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;

    @Override
    public void requestCheck() {

    }

    public UserInteractionDataRequest(@NotNull @Min(1) Long userId) {
        this.userId = userId;
    }

    public UserInteractionDataRequest() {
    }
}
