package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname CAPTCHARequest
 * @Description 验证码请求
 * @Date 2019/10/8 23:05
 * @blame Java Team
 */
@Data
public class CAPTCHARequest extends AbstractRequest {
    @NotNull
    private CAPTCHAReceiver receiver;
    @NotNull
    @Range(min = 1)
    private Long userId;

    @Override
    public void requestCheck() {

    }
}
