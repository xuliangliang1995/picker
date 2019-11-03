package com.grasswort.picker.user.vo;

import com.grasswort.picker.commons.mask.MaskUtil;
import com.grasswort.picker.user.validator.Mobile;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname ChangePhoneForm
 * @Description 修改手机号
 * @Date 2019/11/3 20:58
 * @blame Java Team
 */
@Data
public class ChangePhoneForm {
    @NotNull
    @Mobile
    private String phone;
    @Size(min = 6, max = 6)
    private String captcha;

    @Override
    public String toString() {
        return "ChangePhoneForm{" +
                "phone='" + MaskUtil.maskMobile(phone) + '\'' +
                '}';
    }
}
