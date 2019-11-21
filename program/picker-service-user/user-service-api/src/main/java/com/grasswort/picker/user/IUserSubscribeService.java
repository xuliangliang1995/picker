package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.*;

/**
 * @author xuliangliang
 * @Classname IUserSubscribeService
 * @Description 关注服务
 * @Date 2019/11/21 14:49
 * @blame Java Team
 */
public interface IUserSubscribeService {
    /**
     * 关注作者
     * @param subscribeRequest
     * @return
     */
    SubscribeResponse subscribe(SubscribeRequest subscribeRequest);

    /**
     * 取消作者关注
     * @param unsubscribeRequest
     * @return
     */
    UnsubscribeResponse unsubscribe(UnsubscribeRequest unsubscribeRequest);

    /**
     * 是否关注
     * @param subscribeStatusRequest
     * @return
     */
    SubscribeStatusResponse subscribeStatus(SubscribeStatusRequest subscribeStatusRequest);
}
