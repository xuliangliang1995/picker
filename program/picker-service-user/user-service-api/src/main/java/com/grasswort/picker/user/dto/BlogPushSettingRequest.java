package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogPushSettingRequest
 * @Description 博客推送配置
 * @Date 2019/11/17 14:08
 * @blame Java Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPushSettingRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;

    @Override
    public void requestCheck() {

    }
}
