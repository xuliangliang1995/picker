package com.grasswort.picker.wechat.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname CallBackForm
 * @Description 回调信息
 * @Date 2019/11/15 14:30
 * @blame Java Team
 */
@Data
public class CallBackForm {
    /**
     * json
     * @see com.grasswort.picker.wechat.util.QrcodeInfo
     */
    @NotEmpty
    private String body;
    /**
     * openId
     */
    @Size(min = 28, max = 28)
    @NotNull
    private String openId;
}
