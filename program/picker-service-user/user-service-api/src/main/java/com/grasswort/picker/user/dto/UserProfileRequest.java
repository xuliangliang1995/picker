package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserProfileRequest.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class UserProfileRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;

    public UserProfileRequest() {
    }

    public UserProfileRequest(@NotNull @Min(1) Long userId) {
        this.userId = userId;
    }

    @Override
    public void requestCheck() {

    }
}
