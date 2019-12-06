package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.user.InteractionData;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoResponse
 * @Description 用户基本信息
 * @Date 2019/10/9 11:55
 * @blame Java Team
 */
@Data
public class UserBaseInfoResponse extends AbstractResponse {

    private String authorId;

    private String name;

    private String phone;

    private String email;

    private Byte sex;

    private String avatar;

    private String signature;

    private Boolean bindWechat;

    private String mpNickName;

    private String mpHeadImgUrl;

    private InteractionData interactionData;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
