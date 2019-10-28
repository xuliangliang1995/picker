package com.grasswort.picker.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grasswort.picker.commons.mask.serializer.EmailJsonSerializer;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname SignUpResult
 * @Description 注册 VO
 * @Date 2019/10/15 14:40
 * @blame Java Team
 */
@Data
public class SignUpVO {
    @JsonSerialize(using = EmailJsonSerializer.class)
    private String email;
}
