package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SendActivateEmailRequest
 * @Description TODO
 * @Date 2019/10/8 16:11
 * @blame Java Team
 */
@Data
public class SendActivateEmailRequest extends AbstractRequest {
    @NotNull
    @Length(min = 8, max = 20)
    private String username;
    @Override
    public void requestCheck() {

    }
}
