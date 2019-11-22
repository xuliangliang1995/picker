package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname QrcodeForm
 * @Description 二维码表单
 * @Date 2019/11/22 21:23
 * @blame Java Team
 */
@Data
public class QrcodeForm {
    @NotEmpty
    private String qrcode;
}
