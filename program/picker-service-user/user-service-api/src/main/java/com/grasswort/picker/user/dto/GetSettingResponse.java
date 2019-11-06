package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname GetSettingResponse
 * @Description 获取用户配置
 * @Date 2019/11/4 15:53
 * @blame Java Team
 */
@Data
public class GetSettingResponse extends AbstractResponse {

    private String markdownTheme;

    private Integer safetyCheckMode;

}
