package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.HotWordRequest;
import com.grasswort.picker.blog.dto.HotWordResponse;

/**
 * @author xuliangliang
 * @Classname HotWordService
 * @Description 热词
 * @Date 2019/12/3 14:49
 * @blame Java Team
 */
public interface IHotWordService {

    /**
     * 搜索热词排行
     * @param hotWordRequest
     * @return
     */
    HotWordResponse hotWords(HotWordRequest hotWordRequest);
}
