package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoRequest
 * @Description 获取基本信息请求
 * @Date 2019/10/9 11:54
 * @blame Java Team
 */
@Data
@EqualsAndHashCode
public class UserBaseInfoRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;

    @Override
    public void requestCheck() {

    }
}
