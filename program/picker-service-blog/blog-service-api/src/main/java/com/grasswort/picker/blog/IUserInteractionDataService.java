package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.UserInteractionDataRequest;
import com.grasswort.picker.blog.dto.UserInteractionDataResponse;

/**
 * @author xuliangliang
 * @Classname IUserInteractionDataService
 * @Description 用户博客数据查询（博客总数，获赞数量）
 * @Date 2019/12/3 23:33
 * @blame Java Team
 */
public interface IUserInteractionDataService {
    /**
     * 用户交互数据查询
     * @param interactionDataRequest
     * @return
     */
    UserInteractionDataResponse userInteractionData(UserInteractionDataRequest interactionDataRequest);
}
