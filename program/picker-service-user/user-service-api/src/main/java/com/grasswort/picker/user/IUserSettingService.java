package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.GetSettingRequest;
import com.grasswort.picker.user.dto.GetSettingResponse;
import com.grasswort.picker.user.dto.SaveSettingRequest;
import com.grasswort.picker.user.dto.SaveSettingResponse;

/**
 * @author xuliangliang
 * @Classname IUserSettingService
 * @Description 用户设置
 * @Date 2019/11/4 15:48
 * @blame Java Team
 */
public interface IUserSettingService {
    /**
     * 保存用户设置
     * @param saveSettingRequest
     * @return
     */
    SaveSettingResponse saveSetting(SaveSettingRequest saveSettingRequest);

    /**
     * 获取用户配置
     * @param getSettingRequest
     * @return
     */
    GetSettingResponse getSetting(GetSettingRequest getSettingRequest);
}
