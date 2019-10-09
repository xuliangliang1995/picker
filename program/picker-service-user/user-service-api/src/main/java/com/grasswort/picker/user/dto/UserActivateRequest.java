package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserActivateRequest
 * @Description TODO
 * @Date 2019/10/6 22:31
 * @blame Java Team
 */
@Data
public class UserActivateRequest extends AbstractRequest {
    @NotNull
    @Length(min = 8, max = 20)
    private String username;
    @NotNull
    @Length(min = 32, max = 32)
    private String activationCode;
    @NotNull
    private Long activateId;

    @Override
    public void requestCheck() {

    }
}
