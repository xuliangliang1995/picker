package com.grasswort.picker.user;

/**
 * @author xuliangliang
 * @Classname IUserElasticDocUpdateService
 * @Description 更新 用户 es 存储
 * @Date 2019/12/4 14:39
 * @blame Java Team
 */
public interface IUserElasticDocUpdateService {

    void updateElastic(Long pkUserId);

}
